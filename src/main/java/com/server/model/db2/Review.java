package com.server.model.db2;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @Column(name = "review_id")
    private String id;

    @Column
    private String comment;

    @Column
    private Date date;

    @Column
    private int rating;

    @ManyToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;
}
