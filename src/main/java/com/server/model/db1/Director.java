package com.server.model.db1;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
@Table(schema = "directors")
public class Director {
    @Id
    @Column(name = "director_id")
    private String id;

    @Column
    private String name;

    @Column
    private String nationality;
}
