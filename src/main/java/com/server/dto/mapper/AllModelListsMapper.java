package com.server.dto.mapper;

import com.server.dto.business.AllModelListsDTO;
import com.server.model.management.AllModelLists;

public class AllModelListsMapper {
    public static AllModelListsDTO allModelListsToAllModelListsDTO(AllModelLists allModelLists) {
        AllModelListsDTO allModelListsDTO = new AllModelListsDTO();

        allModelListsDTO.setDirectors(DirectorMapper.directorsToDirectorsDTO(allModelLists.getDirectors()));
        allModelListsDTO.setMovies(MovieMapper.moviesToMoviesDTO(allModelLists.getMovies()));
        allModelListsDTO.setReviews(ReviewMapper.reviewsToReviewsDTO(allModelLists.getReviews()));
        allModelListsDTO.setUsers(UserMapper.usersToUsersDTO(allModelLists.getUsers()));

        return allModelListsDTO;
    }
}
