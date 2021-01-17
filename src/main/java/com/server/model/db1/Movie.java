package com.server.model.db1;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
@Table(schema = "movies")
public class Movie {
    @Id
    @Column(name = "movie_id")
    private String id;

    @Column
    private String title;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private Director director;
}
