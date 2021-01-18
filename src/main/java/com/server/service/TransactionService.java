package com.server.service;


import com.server.model.enums.LockType;
import com.server.model.enums.TransactionStatus;
import com.server.model.management.Lock;
import com.server.model.management.Operation;
import com.server.model.management.Transaction;
import com.server.model.management.WaitForGraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransactionService {
    private static final Logger LOG = LogManager.getLogger(TransactionService.class.getName());

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    private final Queue<Lock> locks = new ConcurrentLinkedQueue<>();
    private final Queue<WaitForGraph> waitForGraphs = new ConcurrentLinkedQueue<>();

    private final DeadlockChecker deadlockChecker = new DeadlockChecker();
    private final AtomicBoolean isDeadlockRunning = new AtomicBoolean(false);
    private final AtomicBoolean oneTransactionAlreadyRolledBack = new AtomicBoolean(false);

    public TransactionService() {
    }

    public void executeTransaction(Transaction transaction) throws InterruptedException {
        LOG.info("Started executing transaction t[" + transaction.getId() + "]");

        transactions.put(transaction.getId(), transaction);

        AtomicBoolean shouldAbort = new AtomicBoolean(false);
        for (Operation operation : transaction.getOperations()) {
            // block resource used by operation and wait if already blocked
            // if lock is acquired, add instance in Locks
            // if blocked, add instance in WaitForGraphs

            Lock lock = new Lock(generateId(), operation.getLockType(), operation.getRecordID(), operation.getTable(), transaction);

            AtomicBoolean wait = new AtomicBoolean(true);
            while (wait.get() && !shouldAbort.get()) {
                List<Transaction> transactionsThatAlreadyHaveLock = getTransactionsThatAlreadyHave(lock);

                if (transactionsThatAlreadyHaveLock.isEmpty()) {
                    // then we can acquire the lock, and add ourselves in the waitForGraph
                    // to show that we have the lock on the resource
                    LOG.info("Transaction t[" + transaction.getId() + "] was added in the waitForGraphs");
                    wait.set(false);
                    WaitForGraph waitForGraph = new WaitForGraph(
                            generateId(),
                            operation.getLockType(),
                            operation.getTable(),
                            operation.getRecordID(),
                            transaction,
                            null);
                    synchronized (waitForGraphs) {
                        waitForGraphs.add(waitForGraph);

                        shouldAbort.set(deadlockChecker.checkForDeadlock(transaction, waitForGraphs));
                        LOG.info("Deadlock checker shouldAbort = " + shouldAbort);
                    }
                } else {
                    // we have to wait for the lock and add ourselves in the waitForGraph
                    // to show that we are waiting for the resource
                    LOG.info("Transaction t[" + transaction.getId() + "] will wait for lock " + operation.getLockType() +
                            ",on resource " + operation.getRecordID() + ", on table " + operation.getTable());
                    wait.set(true);

                    for (Transaction tAlreadyHasLock : transactionsThatAlreadyHaveLock) {
                        synchronized (waitForGraphs) {
                            for (WaitForGraph waitForGraph : waitForGraphs) {
                                if (waitForGraph.getTransactionHasLock().equals(tAlreadyHasLock)) {
                                    waitForGraph.setTransactionWaitsLock(transaction);
                                }
                            }
                        }
                    }

                }
                if (wait.get()) {
                    LOG.info("Transaction t[" + transaction.getId() + "] is waiting...");
                    Thread.sleep(1000);
                    synchronized (waitForGraphs) {
                        shouldAbort.set(deadlockChecker.checkForDeadlock(transaction, waitForGraphs));
                    }
                    LOG.info("Deadlock checker shouldAbort = " + shouldAbort);
                }
            }

            // if should abort, then break
            if (shouldAbort.get() && !oneTransactionAlreadyRolledBack.get()) {
                break;
            }

            // else add the lock to locks and execute operation
            // add instance in locks
            synchronized (locks) {
                locks.add(lock);
            }
            operation.setLock(lock);

            // execute operation
            LOG.info("Executing t[" + transaction.getId() + "] " + operation.getOperationType() + ",on resource " + operation.getRecordID() + ", from table " + operation.getTable());
            operation.execute();
            operation.setExecuted(true);

            if (isDeadlockRunning.get()) {
                Thread.sleep(100);
            }
        }

        if (shouldAbort.get()) {
            if (oneTransactionAlreadyRolledBack.compareAndSet(false, true)) {
                //if aborted, perform rollback
                LOG.info("Performing rollback for transaction t[" + transaction.getId() + "]");
                for (Operation operation : transaction.getOperations()) {
                    if (operation.isExecuted()) {
                        // rollback (execute complementary operation + delete locks)
                        LOG.info("Rollback executed for: " + operation.getOperationType() + ", on resource" + operation.getRecordID() + ", from table " + operation.getTable());
                        operation.rollback();
                        synchronized (locks) {
                            locks.remove(operation.getLock());
                        }
                    }
                }

                LOG.info("Transaction t[" + transaction.getId() + "] aborted");
                transactions.get(transaction.getId()).setTransactionStatus(TransactionStatus.ABORTED);
            } else {
                // strong 2PL (each t holds all its locks until the t terminates (R + W))
                releaseLocksAndCommit(transaction);
            }
        } else {
            releaseLocksAndCommit(transaction);
        }
    }

    public void performDeadlock(Transaction t1, Transaction t2) {
        isDeadlockRunning.set(true);
        Runnable runnableDeadlock1 = () -> {
            try {
                LOG.info("Starting thread 1 for t1");
                LOG.info("Running t1 for thread 1");
                executeTransaction(t1);
            } catch (InterruptedException e) {
                LOG.error("Error occurred: " + e.getMessage());
            }
        };

        Runnable runnableDeadlock2 = () -> {
            try {
                LOG.info("Starting thread 2 for t2");
                LOG.info("Running t2 for thread 2");
                executeTransaction(t2);
            } catch (InterruptedException e) {
                LOG.error("Error occurred: " + e.getMessage());
            }
        };

        Thread thread1 = new Thread(runnableDeadlock1);
        Thread thread2 = new Thread(runnableDeadlock2);

        thread1.start();
        thread2.start();
    }

    public String generateId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void releaseLocksAndCommit(Transaction transaction) {
        // release locks + commit
        LOG.info("Transaction t[" + transaction.getId() + "] will release all locks");
        releaseLocks(transaction);
        LOG.info("Transaction t[" + transaction.getId() + "] committed");
        transactions.get(transaction.getId()).setTransactionStatus(TransactionStatus.COMMITTED);
    }

    private void releaseLocks(Transaction transaction) {
        synchronized (locks) {
            for (Lock lock : locks) {
                if (lock.getTransaction().equals(transaction)) {
                    locks.remove(lock);
                }
            }
        }

        synchronized (waitForGraphs) {
            waitForGraphs.removeIf(waitFor ->
                    (waitFor.getTransactionHasLock() != null && waitFor.getTransactionHasLock().equals(transaction)) ||
                            (waitFor.getTransactionWaitsLock() != null && waitFor.getTransactionWaitsLock().equals(transaction)));
        }
    }

    private List<Transaction> getTransactionsThatAlreadyHave(Lock lockToAcquire) {
        List<Transaction> transactionsThatHaveLock = new ArrayList<>();

        synchronized (locks) {
            for (Lock lock : locks) {
                if (lockTypesInConflict(lock, lockToAcquire)) {
                    transactionsThatHaveLock.add(lock.getTransaction());
                }
            }
        }

        return transactionsThatHaveLock;
    }

    private boolean lockTypesInConflict(Lock lock1, Lock lock2) {
        // will lock in the whole table instead of every record
        String recordId1 = lock1.getRecordID();
        String recordId2 = lock2.getRecordID();

//        if (recordId1 != null && recordId2 != null && recordId1.equals(recordId2) &&
//                lock1.getTableName().equals(lock2.getTableName())) {
        if (lock1.getTableName().equals(lock2.getTableName())) {
            if (lock1.getLockType().equals(lock2.getLockType()) && (lock1.getLockType().equals(LockType.READ))) {
                return false;
            }

            return true;
        }
        return false;
    }

    private static class DeadlockChecker {
        // it tests WFG for cycles continuously, upon each lock wait, instead of periodically (busy wait, for example)
        // returns true if transaction that called checkForDeadlock should be aborted, false otherwise
        public synchronized boolean checkForDeadlock(Transaction transaction, Queue<WaitForGraph> waitForGraphs) {
            // search for corresponding waitForGraph entry from transactions
            WaitForGraph waitForGraphRecordForGivenTransaction = null;
            for (WaitForGraph waitForGraph : waitForGraphs) {
                if (waitForGraph.getTransactionHasLock().equals(transaction)) {
                    waitForGraphRecordForGivenTransaction = waitForGraph;
                    break;
                }
            }

            if (waitForGraphRecordForGivenTransaction == null) {
                return false;
            }

            // check for deadlock using the waitForGraphs and checking for cross waiting between transactions
            for (WaitForGraph waitForGraph : waitForGraphs) {
                if (waitForGraph.getTransactionHasLock().equals(waitForGraphRecordForGivenTransaction.getTransactionWaitsLock()) &&
                        waitForGraph.getTransactionWaitsLock().equals(waitForGraphRecordForGivenTransaction.getTransactionHasLock())) {
                    return true;
                }
            }

            return false;
        }
    }
}
