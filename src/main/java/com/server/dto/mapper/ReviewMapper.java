package com.server.dto.mapper;

import com.server.dto.business.ReviewDTO;
import com.server.model.db2.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {
    public static Review reviewDTOTOReview(ReviewDTO reviewDTO) {
        Review review = new Review();

        review.setId(reviewDTO.getId());
        review.setComment(reviewDTO.getComment());
        review.setDate(reviewDTO.getDate());
        review.setRating(reviewDTO.getRating());

        if (reviewDTO.getUser() != null) {
            review.setUser(UserMapper.userDTOTOUser(reviewDTO.getUser()));
        }

        return review;
    }

    public static ReviewDTO reviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();

        reviewDTO.setId(review.getId());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setDate(review.getDate());
        reviewDTO.setRating(review.getRating());

        if (review.getUser() != null) {
            reviewDTO.setUser(UserMapper.userToUserDTO(review.getUser()));
        }

        return reviewDTO;
    }

    public static List<ReviewDTO> reviewsToReviewsDTO(List<Review> reviews) {
        List<ReviewDTO> reviewsDTO = new ArrayList<>();

        for (Review review : reviews) {
            reviewsDTO.add(reviewToReviewDTO(review));
        }

        return reviewsDTO;
    }
}
