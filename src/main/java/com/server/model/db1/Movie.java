package com.server.model.db1;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Data

@Entity
@Table(schema = "movies")
public class Movie {
    @Id
    @Column(name = "movie_id")
    private String id;

    @Column
    private String title;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.EAGER)
    private Collection<Director> directors = new ArrayList<>();
}
