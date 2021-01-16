package com.server.model.db2;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column
    private String name;

    @Column
    private String email;
}
