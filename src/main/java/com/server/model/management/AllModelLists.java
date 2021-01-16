package com.server.model.management;

import com.server.model.db1.Director;
import com.server.model.db1.Movie;
import com.server.model.db2.Review;
import com.server.model.db2.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllModelLists {
    private List<Movie> movies = new ArrayList<>();
    private List<Director> directors = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private List<User> users = new ArrayList<>();
}
