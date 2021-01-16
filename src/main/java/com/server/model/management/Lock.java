package com.server.model.management;

import com.server.model.enums.LockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lock {
    private String id;
    private LockType lockType;
    private String recordID;
    private String tableName;
    private Transaction transaction;
}
