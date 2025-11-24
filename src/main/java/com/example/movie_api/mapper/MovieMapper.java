package com.example.movie_api.mapper;

import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;

public interface MovieMapper {

    MovieDto toDto(Movie movie);

    Movie toEntity(MovieDto movieDto, Genre genre);

    void updateEntityFromDto(MovieDto dto, Movie entity, Genre genre);
}