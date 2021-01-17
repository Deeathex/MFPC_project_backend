package com.server.model.management;

import com.server.model.enums.LockType;
import com.server.model.enums.OperationType;
import lombok.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Operation<T> {
    private String table;
    private String recordID;
    private Lock lock;
    private LockType lockType;
    private OperationType operationType;
    private OperationType complementaryOperationType;
    private boolean executed = false;

    private DBResult<T> dbResult;
    private JpaRepository<T, String> repository;

    public void execute() {
        executeDBStatement(false, operationType, dbResult.getObject());
    }

    public void rollback() {
        executeDBStatement(true, complementaryOperationType, dbResult.getPreviousObject());
    }

    private void executeDBStatement(boolean isComplementaryOp, OperationType operationType, T entity) {
        switch (operationType) {
            case SELECT:
                dbResult.setObjects(repository.findAll());
                break;
            case INSERT:
                repository.save(entity);
                break;
            case UPDATE:
                if (!isComplementaryOp) {
                    dbResult.setPreviousObject(repository.getOne(recordID));
                }
                repository.save(entity);
                break;
            case DELETE:
                if (!isComplementaryOp) {
                    dbResult.setPreviousObject(repository.getOne(recordID));
                }
                try {
                    repository.deleteById(recordID);
                } catch (EmptyResultDataAccessException e) {
                    //
                }
                break;
            case SELECT_ONE:
                dbResult.setObject(repository.getOne(recordID));
                break;
            default:
                System.err.println("Unsupported operation type");
        }
    }
}
