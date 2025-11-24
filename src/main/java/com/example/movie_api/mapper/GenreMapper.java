package com.example.movie_api.mapper;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.model.Genre;

public interface GenreMapper {

    GenreDto toDto(Genre genre);

    Genre toEntity(GenreDto genreDto);

    void updateEntityFromDto(GenreDto dto, Genre entity);
}
