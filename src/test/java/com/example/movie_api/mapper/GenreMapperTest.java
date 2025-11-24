package com.example.movie_api.mapper;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapperTest {

    private GenreMapper genreMapper;

    @BeforeEach
    void setUp() {
        genreMapper = new GenreMapperImpl();
    }

    @Test
    void toDto_WithValidGenre_ShouldReturnDto() {
        // Arrange
        Genre genre = MapperTestHelper.createTestGenre(1L, "Action", 1000);

        // Act
        GenreDto result = genreMapper.toDto(genre);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Action", result.getName());
        assertEquals(1000, result.getFollowerCount());
    }

    @Test
    void toDto_WithNullGenre_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreMapper.toDto(null)
        );

        assertEquals("Cannot convert null Genre to DTO", exception.getMessage());
    }

    @Test
    void toEntity_WithValidDto_ShouldReturnEntity() {
        // Arrange
        GenreDto dto = GenreDto.builder()
                .id(1L)
                .name("Action")
                .followerCount(1000)
                .build();

        // Act
        Genre result = genreMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Action", result.getName());
        assertEquals(1000, result.getFollowerCount());
    }

    @Test
    void toEntity_WithNullDto_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreMapper.toEntity(null)
        );

        assertEquals("Cannot convert null GenreDto to entity", exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithValidData_ShouldUpdateEntity() {
        // Arrange
        Genre entity = MapperTestHelper.createTestGenre(1L, "Action", 1000);
        GenreDto dto = GenreDto.builder()
                .name("Action/Adventure")
                .followerCount(1500)
                .build();

        // Act
        genreMapper.updateEntityFromDto(dto, entity);

        // Assert
        assertEquals("Action/Adventure", entity.getName());
        assertEquals(1500, entity.getFollowerCount());
        assertEquals(1L, entity.getId()); // ID should not change
    }

    @Test
    void updateEntityFromDto_WithNullDto_ShouldThrowException() {
        // Arrange
        Genre entity = MapperTestHelper.createTestGenre(1L, "Action", 1000);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreMapper.updateEntityFromDto(null, entity)
        );

        assertEquals("Cannot update entity from null DTO", exception.getMessage());
    }

    @Test
    void updateEntityFromDto_WithNullEntity_ShouldThrowException() {
        // Arrange
        GenreDto dto = GenreDto.builder()
                .name("Action")
                .followerCount(1000)
                .build();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> genreMapper.updateEntityFromDto(dto, null)
        );

        assertEquals("Cannot update null entity", exception.getMessage());
    }
}
