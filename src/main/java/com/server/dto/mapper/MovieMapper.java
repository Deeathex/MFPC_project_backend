package com.server.dto.mapper;

import com.server.dto.business.DirectorDTO;
import com.server.dto.business.MovieDTO;
import com.server.model.db1.Director;
import com.server.model.db1.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {
    public static Movie movieDTOTOMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();

        movie.setId(movieDTO.getId());
        movie.setTitle(movieDTO.getTitle());
        movie.setDirectors(DirectorMapper.directorsDTOToDirectors((List<DirectorDTO>) movieDTO.getDirectors()));

        return movie;
    }

    public static MovieDTO movieToMovieDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId(movie.getId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDirectors(DirectorMapper.directorsToDirectorsDTO((List<Director>) movie.getDirectors()));

        return movieDTO;
    }

    public static List<MovieDTO> moviesToMoviesDTO(List<Movie> movies) {
        List<MovieDTO> moviesDTO = new ArrayList<>();

        for (Movie movie : movies) {
            moviesDTO.add(movieToMovieDTO(movie));
        }

        return moviesDTO;
    }
}
