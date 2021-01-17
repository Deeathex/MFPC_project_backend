package com.server.controller;

import com.server.dto.business.*;
import com.server.dto.mapper.*;
import com.server.model.db1.Director;
import com.server.model.db1.Movie;
import com.server.model.db2.Review;
import com.server.model.db2.User;
import com.server.service.BusinessService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "tds")
public class Controller {
    private static final Logger LOG = LogManager.getLogger(Controller.class.getName());

    private final BusinessService businessService;

    public Controller(BusinessService businessService) {
        this.businessService = businessService;
    }


    // 6-8 Cazuri de utilizare
    @PostMapping("/movie/director")
    public ResponseEntity<?> addMovieAndDirector_t2(@RequestBody MovieAndDirectorDTO movieAndDirectorDTO) {
        LOG.log(Level.INFO, "addMovieAndDirector_t2 called");

        Movie movie = MovieMapper.movieDTOTOMovie(movieAndDirectorDTO.getMovie());
        Director director = DirectorMapper.directorDTOTODirector((movieAndDirectorDTO.getDirector()));
        businessService.addMovieAndDirector(movie, director);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/review/user")
    public ResponseEntity<?> addReviewAndUser_t3(@RequestBody ReviewAndUserDTO reviewAndUserDTO) {
        LOG.log(Level.INFO, "addReviewAndUser_t3 called");

        Review review = ReviewMapper.reviewDTOTOReview(reviewAndUserDTO.getReview());
        User user = UserMapper.userDTOTOUser((reviewAndUserDTO.getUser()));
        businessService.addReviewAndUser(review, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/movie/{movieId}/director/{directorId}")
    public ResponseEntity<?> deleteMovieAndDirector_t4(@PathVariable String movieId, @PathVariable String directorId) {
        LOG.log(Level.INFO, "deleteMovieAndDirector_t4 called");

        businessService.deleteMovieAndDirector(movieId, directorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/review/{reviewId}/user/{userId}")
    public ResponseEntity<?> deleteReviewAndUser_t5(@PathVariable String reviewId, @PathVariable String userId) {
        LOG.log(Level.INFO, "deleteMovieAndDirector_t4 called");

        businessService.deleteReviewAndUser(reviewId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get-movies-directors")
    public ResponseEntity<?> getMoviesAndDirectors_t6() {
        AllModelListsDTO allModelListsDTO = AllModelListsMapper.allModelListsToAllModelListsDTO(businessService.getMoviesAndDirectors());

        return new ResponseEntity<>(allModelListsDTO, HttpStatus.OK);
    }

    @GetMapping("/get-reviews-users")
    public ResponseEntity<?> getReviewsAndUsers_t7() {
        AllModelListsDTO allModelListsDTO = AllModelListsMapper.allModelListsToAllModelListsDTO(businessService.getReviewsAndUsers());

        return new ResponseEntity<>(allModelListsDTO, HttpStatus.OK);
    }

    @PostMapping("/deadlock")
    public ResponseEntity<?> deadlock(@RequestBody DirectorsAndUsersDTO directorsAndUsersDTO) {
        Director director1 = DirectorMapper.directorDTOTODirector(directorsAndUsersDTO.getDirectors().get(0));
        User user1 = UserMapper.userDTOTOUser(directorsAndUsersDTO.getUsers().get(0));
        Director director2 = DirectorMapper.directorDTOTODirector(directorsAndUsersDTO.getDirectors().get(1));
        User user2 = UserMapper.userDTOTOUser(directorsAndUsersDTO.getUsers().get(1));
        this.businessService.performDeadlock(director1, user1, director2, user2);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
