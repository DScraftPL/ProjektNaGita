package com.example.movie_api.service;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.mapper.GenreMapper;
import com.example.movie_api.model.Genre;
import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.validation.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private GenreMapper genreMapper;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private GenreService genreService;

    private Genre testGenre;
    private GenreDto testGenreDto;

    @BeforeEach
    void setUp() {
        testGenre = Genre.builder()
                .id(1L)
                .name("Action")
                .followerCount(1000)
                .build();

        testGenreDto = GenreDto.builder()
                .id(1L)
                .name("Action")
                .followerCount(1000)
                .build();
    }

    @Test
    void createGenre_WithValidData_ShouldReturnGenreDto() {
        doNothing().when(validationService).validateGenreName(anyString());
        doNothing().when(validationService).validateFollowerCount(any(Integer.class));
        when(genreRepository.existsByName(testGenreDto.getName())).thenReturn(false);
        when(genreMapper.toEntity(testGenreDto)).thenReturn(testGenre);
        when(genreRepository.save(any(Genre.class))).thenReturn(testGenre);
        when(genreMapper.toDto(testGenre)).thenReturn(testGenreDto);

        GenreDto result = genreService.createGenre(testGenreDto);

        assertNotNull(result);
        assertEquals("Action", result.getName());
        assertEquals(1000, result.getFollowerCount());
        verify(validationService, times(1)).validateGenreName(anyString());
        verify(validationService, times(1)).validateFollowerCount(any(Integer.class));
        verify(genreRepository, times(1)).save(any(Genre.class));
        verify(genreMapper, times(1)).toDto(testGenre);
    }

    @Test
    void createGenre_WithDuplicateName_ShouldThrowException() {
        doNothing().when(validationService).validateGenreName(anyString());
        doNothing().when(validationService).validateFollowerCount(any(Integer.class));
        when(genreRepository.existsByName(testGenreDto.getName())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            genreService.createGenre(testGenreDto);
        });

        verify(genreRepository, never()).save(any(Genre.class));
    }

    @Test
    void findAllGenres_ShouldReturnListOfGenres() {
        Genre genre2 = Genre.builder().id(2L).name("Drama").followerCount(800).build();
        GenreDto genreDto2 = GenreDto.builder().id(2L).name("Drama").followerCount(800).build();

        when(genreRepository.findAll()).thenReturn(Arrays.asList(testGenre, genre2));
        when(genreMapper.toDto(testGenre)).thenReturn(testGenreDto);
        when(genreMapper.toDto(genre2)).thenReturn(genreDto2);

        List<GenreDto> result = genreService.findAllGenres();

        assertEquals(2, result.size());
        verify(genreRepository, times(1)).findAll();
        verify(genreMapper, times(2)).toDto(any(Genre.class));
    }

    @Test
    void findGenreById_WithExistingId_ShouldReturnGenreDto() {
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(genreMapper.toDto(testGenre)).thenReturn(testGenreDto);

        GenreDto result = genreService.findGenreById(1L);

        assertNotNull(result);
        assertEquals("Action", result.getName());
        verify(genreRepository, times(1)).findById(1L);
        verify(genreMapper, times(1)).toDto(testGenre);
    }

    @Test
    void findGenreById_WithNonExistingId_ShouldThrowException() {
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.findGenreById(999L);
        });
    }

    @Test
    void updateGenre_WithValidData_ShouldReturnUpdatedGenre() {
        GenreDto updateDto = GenreDto.builder()
                .name("Action/Adventure")
                .followerCount(1500)
                .build();

        doNothing().when(validationService).validateGenreName(anyString());
        doNothing().when(validationService).validateFollowerCount(any(Integer.class));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(genreRepository.existsByName("Action/Adventure")).thenReturn(false);
        doNothing().when(genreMapper).updateEntityFromDto(updateDto, testGenre);
        when(genreRepository.save(testGenre)).thenReturn(testGenre);
        when(genreMapper.toDto(testGenre)).thenReturn(updateDto);

        GenreDto result = genreService.updateGenre(1L, updateDto);

        assertNotNull(result);
        verify(genreMapper, times(1)).updateEntityFromDto(updateDto, testGenre);
        verify(genreRepository, times(1)).save(testGenre);
    }

    @Test
    void deleteGenre_WithExistingId_ShouldDeleteGenre() {
        when(genreRepository.existsById(1L)).thenReturn(true);

        genreService.deleteGenre(1L);

        verify(genreRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteGenre_WithNonExistingId_ShouldThrowException() {
        when(genreRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            genreService.deleteGenre(999L);
        });
    }
}
