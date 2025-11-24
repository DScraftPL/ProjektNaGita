package com.example.movie_api.mapper;


import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;

/**
 * Test helper utility for creating test entities.
 * Used to avoid service dependencies in mapper tests.
 */
public class MapperTestHelper {

    public static Genre createTestGenre(Long id, String name, Integer followerCount) {
        return Genre.builder()
                .id(id)
                .name(name)
                .followerCount(followerCount)
                .build();
    }

    public static Movie createTestMovie(Long id, String title, String director,
                                        Double rating, Genre genre) {
        return Movie.builder()
                .id(id)
                .title(title)
                .director(director)
                .rating(rating)
                .genre(genre)
                .build();
    }
}
