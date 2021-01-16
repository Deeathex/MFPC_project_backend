package com.server.model.management;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DBResult<T> {
    private T object;
    private T previousObject;
    private List<T> objects;
}
