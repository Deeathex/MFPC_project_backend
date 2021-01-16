# Tranzactional distributed system strict 2PL (Two Phase Locking) implementation

## Requirements   

Project - a distributed concurrent application of student's choice. The application must follow the above 
requirements:
- it has to be distributed, but not simple, client-server, multi layer architecture (client/web - 
business/middleware - data, etc)
- it has to deal with concurrent aspects at data level (i.e. transactions in data base)
- it must use 2 different data bases (at least 3 tables) and it has to use distributed transactions
(it is not a must to use 2 distinct data base servers)
- it must have at least 6-8 use cases/operations  

Important: The application must implement a distributed transaction at application level: you can consider 
that a transaction consists of simple SQL operations (at least 3 SQL instructions: insert, delete, update, 
select). Those SQL operations would apply on different tables. You must assure, at application level,
the ACID properties of this transaction. The application should implement the following:

- a scheduling algorithm (i.e. concurrency control algorithm) from the ones discussed in class
(lock or, timestamp ordering related, etc); the scheduling algorithm must be distributed (e.g. 2 Phase Commit,
distributed 2 Phase Locking, or timestamp distributed ordering);
- a rollback mechanism (multiversions, rollback for every SQL instruction, etc)
- a commit mechanism 
- a detection and fix mechanism for deadlocks (graphs, conflict lists, etc)

 ## ACID properties
 
### Atomicity
All changes to data are performed as if they are a single operation. That is, all the changes are performed, or none of them are.
For example, in an application that transfers funds from one account to another, the atomicity property ensures that, if a debit is made successfully from one account, the corresponding credit is made to the other account.
### Consistency
Data is in a consistent state when a transaction starts and when it ends.
For example, in an application that transfers funds from one account to another, the consistency property ensures that the total value of funds in both the accounts is the same at the start and end of each transaction.
### Isolation
The intermediate state of a transaction is invisible to other transactions. As a result, transactions that run concurrently appear to be serialized.
For example, in an application that transfers funds from one account to another, the isolation property ensures that another transaction sees the transferred funds in one account or the other, but not in both, nor in neither.
### Durability
After a transaction successfully completes, changes to data persist and are not undone, even in the event of a system failure.
For example, in an application that transfers funds from one account to another, the durability property ensures that the changes made to each account will not be reversed. 
