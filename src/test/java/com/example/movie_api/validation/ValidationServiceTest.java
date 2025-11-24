package com.example.movie_api.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    // ========== Rating Validation Tests ==========

    @Test
    void validateRating_WithValidRating_ShouldNotThrowException() {
        // Valid ratings within range
        assertDoesNotThrow(() -> validationService.validateRating(0.0));
        assertDoesNotThrow(() -> validationService.validateRating(5.0));
        assertDoesNotThrow(() -> validationService.validateRating(10.0));
        assertDoesNotThrow(() -> validationService.validateRating(7.5));
    }

    @Test
    void validateRating_WithNullRating_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateRating(null)
        );

        assertEquals("Rating cannot be null", exception.getMessage());
    }

    @Test
    void validateRating_WithNegativeRating_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateRating(-1.0)
        );

        assertTrue(exception.getMessage().contains("Rating must be between"));
    }

    @Test
    void validateRating_WithRatingAboveMax_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateRating(11.0)
        );

        assertTrue(exception.getMessage().contains("Rating must be between"));
    }

    @Test
    void validateRating_WithBoundaryValues_ShouldWork() {
        // Test exact boundaries
        assertDoesNotThrow(() -> validationService.validateRating(0.0));
        assertDoesNotThrow(() -> validationService.validateRating(10.0));

        // Just outside boundaries
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateRating(-0.1));
        assertThrows(IllegalArgumentException.class,
                () -> validationService.validateRating(10.1));
    }

    // ========== Genre Name Validation Tests ==========

    @Test
    void validateGenreName_WithValidName_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validationService.validateGenreName("Action"));
        assertDoesNotThrow(() -> validationService.validateGenreName("Sci-Fi"));
        assertDoesNotThrow(() -> validationService.validateGenreName("Action/Adventure"));
        assertDoesNotThrow(() -> validationService.validateGenreName("A"));
    }

    @Test
    void validateGenreName_WithNullName_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateGenreName(null)
        );

        assertEquals("Genre name cannot be empty", exception.getMessage());
    }

    @Test
    void validateGenreName_WithEmptyName_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateGenreName("")
        );

        assertEquals("Genre name cannot be empty", exception.getMessage());
    }

    @Test
    void validateGenreName_WithBlankName_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateGenreName("   ")
        );

        assertEquals("Genre name cannot be empty", exception.getMessage());
    }

    @Test
    void validateGenreName_WithTooLongName_ShouldThrowException() {
        String longName = "A".repeat(101);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateGenreName(longName)
        );

        assertEquals("Genre name cannot exceed 100 characters", exception.getMessage());
    }

    @Test
    void validateGenreName_WithMaxLengthName_ShouldNotThrowException() {
        String maxLengthName = "A".repeat(100);

        assertDoesNotThrow(() -> validationService.validateGenreName(maxLengthName));
    }

    // ========== Follower Count Validation Tests ==========

    @Test
    void validateFollowerCount_WithValidCount_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validationService.validateFollowerCount(0));
        assertDoesNotThrow(() -> validationService.validateFollowerCount(100));
        assertDoesNotThrow(() -> validationService.validateFollowerCount(1000000));
    }

    @Test
    void validateFollowerCount_WithNullCount_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateFollowerCount(null)
        );

        assertEquals("Follower count cannot be null", exception.getMessage());
    }

    @Test
    void validateFollowerCount_WithNegativeCount_ShouldThrowException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateFollowerCount(-1)
        );

        assertEquals("Follower count cannot be negative", exception.getMessage());
    }

    @Test
    void validateFollowerCount_WithZeroCount_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validationService.validateFollowerCount(0));
    }
}
