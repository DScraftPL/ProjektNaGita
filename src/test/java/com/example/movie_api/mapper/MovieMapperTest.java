package com.example.movie_api.mapper;

import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieMapperTest {

    private MovieMapper movieMapper;
    private Genre testGenre;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();
        testGenre = MapperTestHelper.createTestGenre(1L, "Action", 1000);
    }

    @Test
    void toDto_WithValidMovie_ShouldReturnDto() {
        // Arrange
        Movie movie = MapperTestHelper.createTestMovie(
                1L, "The Matrix", "Wachowski", 8.7, testGenre
        );

        // Act
        MovieDto result = movieMapper.toDto(movie);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("The Matrix", result.getTitle());
        assertEquals("Wachowski", result.getDirector());
        assertEquals(8.7, result.getRating());
        assertEquals(1L, result.getGenreId());
        assertEquals("Action", result.getGenreName());
    }

    @Test
    void toDto_WithNullMovie_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.toDto(null)
        );

        assertEquals("Cannot convert null Movie to DTO", exception.getMessage());
    }

    @Test
    void toEntity_WithValidDto_ShouldReturnEntity() {
        // Arrange
        MovieDto dto = MovieDto.builder()
                .id(1L)
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genreId(1L)
                .build();

        // Act
        Movie result = movieMapper.toEntity(dto, testGenre);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("The Matrix", result.getTitle());
        assertEquals("Wachowski", result.getDirector());
        assertEquals(8.7, result.getRating());
        assertEquals(testGenre, result.getGenre());
    }

    @Test
    void toEntity_WithNullDto_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.toEntity(null, testGenre)
        );

        assertEquals("Cannot convert null MovieDto to entity", exception.getMessage());
    }

    @Test
    void toEntity_WithNullGenre_ShouldThrowException() {
        // Arrange
        MovieDto dto = MovieDto.builder()
                .id(1L)
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genreId(1L)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.toEntity(dto, null)
        );

        assertEquals("Genre cannot be null when creating Movie entity", exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithValidData_ShouldUpdateEntity() {
        // Arrange
        Movie entity = MapperTestHelper.createTestMovie(
                1L, "The Matrix", "Wachowski", 8.7, testGenre
        );

        Genre newGenre = MapperTestHelper.createTestGenre(2L, "Sci-Fi", 500);

        MovieDto dto = MovieDto.builder()
                .title("The Matrix Reloaded")
                .director("Wachowski Sisters")
                .rating(7.2)
                .genreId(2L)
                .build();

        // Act
        movieMapper.updateEntityFromDto(dto, entity, newGenre);

        // Assert
        assertEquals("The Matrix Reloaded", entity.getTitle());
        assertEquals("Wachowski Sisters", entity.getDirector());
        assertEquals(7.2, entity.getRating());
        assertEquals(newGenre, entity.getGenre());
        assertEquals(1L, entity.getId()); // ID should not change
    }

    @Test
    void updateEntityFromDto_WithNullDto_ShouldThrowException() {
        // Arrange
        Movie entity = MapperTestHelper.createTestMovie(
                1L, "The Matrix", "Wachowski", 8.7, testGenre
        );

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.updateEntityFromDto(null, entity, testGenre)
        );

        assertEquals("Cannot update entity from null DTO", exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithNullEntity_ShouldThrowException() {
        // Arrange
        MovieDto dto = MovieDto.builder()
                .title("Test Movie")
                .director("Test Director")
                .rating(8.0)
                .genreId(1L)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.updateEntityFromDto(dto, null, testGenre)
        );

        assertEquals("Cannot update null entity", exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithNullGenre_ShouldThrowException() {
        // Arrange
        Movie entity = MapperTestHelper.createTestMovie(
                1L, "The Matrix", "Wachowski", 8.7, testGenre
        );

        MovieDto dto = MovieDto.builder()
                .title("Test Movie")
                .director("Test Director")
                .rating(8.0)
                .genreId(1L)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieMapper.updateEntityFromDto(dto, entity, null)
        );

        assertEquals("Genre cannot be null when updating Movie entity", exception.getMessage());
    }
}