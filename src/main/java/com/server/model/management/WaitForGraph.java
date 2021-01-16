package com.server.model.management;

import com.server.model.enums.LockType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitForGraph {
    private String id;
    private LockType lockType;
    private String lockTable;
    private String lockObject;
    private Transaction transactionHasLock;
    private Transaction transactionWaitsLock;
}
