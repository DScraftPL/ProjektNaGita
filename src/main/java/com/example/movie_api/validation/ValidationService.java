package com.example.movie_api.validation;

public interface ValidationService {

    void validateRating(Double rating);

    void validateGenreName(String name);

    void validateFollowerCount(Integer count);
}
