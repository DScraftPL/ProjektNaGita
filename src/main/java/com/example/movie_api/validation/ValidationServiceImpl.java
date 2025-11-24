package com.example.movie_api.validation;

import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

    private static final double MIN_RATING = 0.0;
    private static final double MAX_RATING = 10.0;
    private static final int MIN_FOLLOWERS = 0;

    @Override
    public void validateRating(Double rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Rating cannot be null");
        }

        if (rating < MIN_RATING || rating > MAX_RATING) {
            throw new IllegalArgumentException(
                    String.format("Rating must be between %.1f and %.1f", MIN_RATING, MAX_RATING)
            );
        }
    }

    @Override
    public void validateGenreName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name cannot be empty");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Genre name cannot exceed 100 characters");
        }
    }

    @Override
    public void validateFollowerCount(Integer count) {
        if (count == null) {
            throw new IllegalArgumentException("Follower count cannot be null");
        }

        if (count < MIN_FOLLOWERS) {
            throw new IllegalArgumentException("Follower count cannot be negative");
        }
    }
}