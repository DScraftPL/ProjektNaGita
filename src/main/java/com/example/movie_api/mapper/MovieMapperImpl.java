package com.example.movie_api.mapper;

import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieDto toDto(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Cannot convert null Movie to DTO");
        }

        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .director(movie.getDirector())
                .rating(movie.getRating())
                .genreId(movie.getGenre().getId())
                .genreName(movie.getGenre().getName())
                .build();
    }

    @Override
    public Movie toEntity(MovieDto dto, Genre genre) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot convert null MovieDto to entity");
        }

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null when creating Movie entity");
        }

        return Movie.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .director(dto.getDirector())
                .rating(dto.getRating())
                .genre(genre)
                .build();
    }

    @Override
    public void updateEntityFromDto(MovieDto dto, Movie entity, Genre genre) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot update entity from null DTO");
        }

        if (entity == null) {
            throw new IllegalArgumentException("Cannot update null entity");
        }

        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null when updating Movie entity");
        }

        entity.setTitle(dto.getTitle());
        entity.setDirector(dto.getDirector());
        entity.setRating(dto.getRating());
        entity.setGenre(genre);
    }
}