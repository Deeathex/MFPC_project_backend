package com.server.service;

import com.server.dto.business.AllModelListsDTO;
import com.server.model.db1.Director;
import com.server.model.db1.Movie;
import com.server.model.db2.Review;
import com.server.model.db2.User;
import com.server.model.enums.LockType;
import com.server.model.enums.OperationType;
import com.server.model.enums.TransactionStatus;
import com.server.model.management.AllModelLists;
import com.server.model.management.DBResult;
import com.server.model.management.Operation;
import com.server.model.management.Transaction;
import com.server.repository.db1.DirectorRepository;
import com.server.repository.db1.MovieRepository;
import com.server.repository.db2.ReviewRepository;
import com.server.repository.db2.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessService {
    private final TransactionService transactionService;

    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public BusinessService(DirectorRepository directorRepository, MovieRepository movieRepository,
                           ReviewRepository reviewRepository, UserRepository userRepository) {
        this.directorRepository = directorRepository;
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;

        this.transactionService = new TransactionService();
    }

    public void addMovieForDirector(String directorId, Movie movie) {
        List<Operation> operations = new ArrayList<>();

        Operation<Director> operation1 = new Operation<>();
        operation1.setTable("directors");
        operation1.setRecordID(directorId);
        operation1.setLockType(LockType.READ);
        operation1.setOperationType(OperationType.SELECT_ONE);
        operation1.setRepository(directorRepository);

        operations.add(operation1);

        Operation<Movie> operation2 = new Operation<>();
        operation2.setTable("movies");
        operation2.setRecordID(movie.getId());
        operation2.setLockType(LockType.WRITE);
        operation2.setOperationType(OperationType.INSERT);
        operation2.setComplementaryOperationType(OperationType.DELETE);
        operation2.setRepository(movieRepository);
        DBResult<Movie> dbResult = new DBResult<>();
        dbResult.setObject(movie);
        operation2.setDbResult(dbResult);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);
    }

    public void addMovieAndDirector(Movie movie, Director director) {
        List<Operation> operations = new ArrayList<>();

        Operation<Movie> operation1 = new Operation<>();
        operation1.setTable("movies");
        operation1.setRecordID(movie.getId());
        operation1.setLockType(LockType.WRITE);
        operation1.setOperationType(OperationType.INSERT);
        operation1.setComplementaryOperationType(OperationType.DELETE);
        operation1.setRepository(movieRepository);
        DBResult<Movie> dbResult1 = new DBResult<>();
        dbResult1.setObject(movie);
        operation1.setDbResult(dbResult1);

        operations.add(operation1);

        String directorId = transactionService.generateId();
        Operation<Director> operation2 = new Operation<>();
        operation2.setTable("directors");
        operation2.setRecordID(directorId);
        operation2.setLockType(LockType.WRITE);
        operation2.setOperationType(OperationType.INSERT);
        operation2.setComplementaryOperationType(OperationType.DELETE);
        operation2.setRepository(directorRepository);
        DBResult<Director> dbResult2 = new DBResult<>();
        dbResult2.setObject(director);
        operation2.setDbResult(dbResult2);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);
    }

    public void addReviewAndUser(Review review, User user) {
        List<Operation> operations = new ArrayList<>();

        Operation<Review> operation1 = new Operation<>();
        operation1.setTable("reviews");
        operation1.setRecordID(review.getId());
        operation1.setLockType(LockType.WRITE);
        operation1.setOperationType(OperationType.INSERT);
        operation1.setComplementaryOperationType(OperationType.DELETE);
        operation1.setRepository(reviewRepository);
        DBResult<Review> dbResult1 = new DBResult<>();
        dbResult1.setObject(review);
        operation1.setDbResult(dbResult1);

        operations.add(operation1);

        Operation<User> operation2 = new Operation<>();
        operation2.setTable("users");
        operation2.setRecordID(user.getId());
        operation2.setLockType(LockType.WRITE);
        operation2.setOperationType(OperationType.INSERT);
        operation2.setComplementaryOperationType(OperationType.DELETE);
        operation2.setRepository(userRepository);
        DBResult<User> dbResult2 = new DBResult<>();
        dbResult2.setObject(user);
        operation2.setDbResult(dbResult2);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);
    }

    public void deleteMovieAndDirector(String movieId, String directorId) {
        List<Operation> operations = new ArrayList<>();

        Operation<Movie> operation1 = new Operation<>();
        operation1.setTable("movies");
        operation1.setRecordID(movieId);
        operation1.setLockType(LockType.WRITE);
        operation1.setOperationType(OperationType.DELETE);
        operation1.setComplementaryOperationType(OperationType.INSERT);
        operation1.setRepository(movieRepository);

        operations.add(operation1);

        Operation<Director> operation2 = new Operation<>();
        operation2.setTable("directors");
        operation2.setRecordID(directorId);
        operation2.setLockType(LockType.WRITE);
        operation2.setOperationType(OperationType.DELETE);
        operation2.setComplementaryOperationType(OperationType.INSERT);
        operation2.setRepository(directorRepository);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);
    }

    public void deleteReviewAndUser(String reviewId, String userId) {
        List<Operation> operations = new ArrayList<>();

        Operation<Review> operation1 = new Operation<>();
        operation1.setTable("reviews");
        operation1.setRecordID(reviewId);
        operation1.setLockType(LockType.WRITE);
        operation1.setOperationType(OperationType.DELETE);
        operation1.setComplementaryOperationType(OperationType.INSERT);
        operation1.setRepository(reviewRepository);

        operations.add(operation1);

        Operation<User> operation2 = new Operation<>();
        operation2.setTable("users");
        operation2.setRecordID(userId);
        operation2.setLockType(LockType.WRITE);
        operation2.setOperationType(OperationType.DELETE);
        operation2.setComplementaryOperationType(OperationType.INSERT);
        operation2.setRepository(userRepository);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);
    }

    public AllModelLists getMoviesAndDirectors() {
        AllModelLists allModelLists = new AllModelLists();
        List<Operation> operations = new ArrayList<>();

        Operation<Movie> operation1 = new Operation<>();
        operation1.setTable("movies");
        operation1.setLockType(LockType.READ);
        operation1.setOperationType(OperationType.SELECT);
        operation1.setRepository(movieRepository);
        DBResult<Movie> dbResult1 = new DBResult<>();
        operation1.setDbResult(dbResult1);

        operations.add(operation1);

        Operation<Director> operation2 = new Operation<>();
        operation2.setTable("directors");
        operation2.setLockType(LockType.READ);
        operation2.setOperationType(OperationType.SELECT);
        operation2.setRepository(directorRepository);
        DBResult<Director> dbResult2 = new DBResult<>();
        operation2.setDbResult(dbResult2);

        operations.add(operation2);
        generateAndExecuteTransaction(operations);

        allModelLists.setMovies(dbResult1.getObjects());
        allModelLists.setDirectors(dbResult2.getObjects());

        return allModelLists;
    }

    public AllModelLists getReviewsAndUsers() {
        AllModelLists allModelLists = new AllModelLists();
        List<Operation> operations = new ArrayList<>();

        Operation<Review> operation1 = new Operation<>();
        operation1.setTable("reviews");
        operation1.setLockType(LockType.READ);
        operation1.setOperationType(OperationType.SELECT);
        operation1.setRepository(reviewRepository);
        DBResult<Review> dbResult1 = new DBResult<>();
        operation1.setDbResult(dbResult1);

        operations.add(operation1);

        Operation<User> operation2 = new Operation<>();
        operation2.setTable("users");
        operation2.setLockType(LockType.READ);
        operation2.setOperationType(OperationType.SELECT);
        operation2.setRepository(userRepository);
        DBResult<User> dbResult2 = new DBResult<>();
        operation2.setDbResult(dbResult2);

        operations.add(operation2);

        generateAndExecuteTransaction(operations);

        allModelLists.setReviews(dbResult1.getObjects());
        allModelLists.setUsers(dbResult2.getObjects());

        return allModelLists;
    }

    public void performDeadlock(Director director1, User user1, Director director2, User user2) {
        // t1 performs add director add user
        List<Operation> operations_t1 = new ArrayList<>();

        Operation<Director> operation1_t1 = new Operation<>();
        operation1_t1.setTable("directors");
        operation1_t1.setRecordID(director1.getId());
        operation1_t1.setLockType(LockType.WRITE);
        operation1_t1.setOperationType(OperationType.INSERT);
        operation1_t1.setComplementaryOperationType(OperationType.DELETE);
        operation1_t1.setRepository(directorRepository);
        DBResult<Director> dbResult1_t1 = new DBResult<>();
        dbResult1_t1.setObject(director1);
        operation1_t1.setDbResult(dbResult1_t1);

        operations_t1.add(operation1_t1);

        Operation<User> operation2_t1 = new Operation<>();
        operation2_t1.setTable("users");
        operation2_t1.setRecordID(user1.getId());
        operation2_t1.setLockType(LockType.WRITE);
        operation2_t1.setOperationType(OperationType.INSERT);
        operation2_t1.setComplementaryOperationType(OperationType.DELETE);
        operation2_t1.setRepository(userRepository);
        DBResult<User> dbResult2_t1 = new DBResult<>();
        dbResult2_t1.setObject(user1);
        operation2_t1.setDbResult(dbResult2_t1);

        operations_t1.add(operation2_t1);

        // t2 performs add user add director
        List<Operation> operations_t2 = new ArrayList<>();

        Operation<User> operation1_t2 = new Operation<>();
        operation1_t2.setTable("users");
        operation1_t2.setRecordID(user2.getId());
        operation1_t2.setLockType(LockType.WRITE);
        operation1_t2.setOperationType(OperationType.INSERT);
        operation1_t2.setComplementaryOperationType(OperationType.DELETE);
        operation1_t2.setRepository(userRepository);
        DBResult<User> dbResult1_t2 = new DBResult<>();
        dbResult1_t2.setObject(user2);
        operation1_t2.setDbResult(dbResult2_t1);

        operations_t2.add(operation1_t2);

        Operation<Director> operation2_t2 = new Operation<>();
        operation2_t2.setTable("directors");
        operation2_t2.setRecordID(director2.getId());
        operation2_t2.setLockType(LockType.WRITE);
        operation2_t2.setOperationType(OperationType.INSERT);
        operation2_t2.setComplementaryOperationType(OperationType.DELETE);
        operation2_t2.setRepository(directorRepository);
        DBResult<Director> dbResult2_t2 = new DBResult<>();
        dbResult2_t2.setObject(director2);
        operation2_t2.setDbResult(dbResult2_t2);

        operations_t2.add(operation2_t2);

        performDeadlock(operations_t1, operations_t2);
    }

    private void performDeadlock(List<Operation> operationsFirstTransaction, List<Operation> operationsSecondTransaction) {
        Transaction t1 = createTransaction(operationsFirstTransaction);
        Transaction t2 = createTransaction(operationsSecondTransaction);
        transactionService.performDeadlock(t1, t2);
    }

    private void generateAndExecuteTransaction(List<Operation> operations) {
        try {
            transactionService.executeTransaction(createTransaction(operations));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Transaction createTransaction(List<Operation> operations) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionService.generateId());
        transaction.setTimestamp(System.currentTimeMillis());
        transaction.setTransactionStatus(TransactionStatus.ACTIVE);
        transaction.setOperations(operations);

        return transaction;
    }
}
