package com.example.movie_api.service;

import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.mapper.MovieMapper;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.repository.MovieRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private MovieService movieService;

    private Genre testGenre;
    private Movie testMovie;
    private MovieDto testMovieDto;

    @BeforeEach
    void setUp() {
        testGenre = Genre.builder()
                .id(1L)
                .name("Action")
                .followerCount(1000)
                .build();

        testMovie = Movie.builder()
                .id(1L)
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();

        testMovieDto = MovieDto.builder()
                .id(1L)
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genreId(1L)
                .genreName("Action")
                .build();
    }

    @Test
    void createMovie_WithValidData_ShouldReturnMovieDto() {
        doNothing().when(validationService).validateRating(any(Double.class));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        when(movieMapper.toEntity(testMovieDto, testGenre)).thenReturn(testMovie);
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);
        when(movieMapper.toDto(testMovie)).thenReturn(testMovieDto);

        MovieDto result = movieService.createMovie(testMovieDto);

        assertNotNull(result);
        assertEquals("The Matrix", result.getTitle());
        assertEquals("Wachowski", result.getDirector());
        assertEquals(8.7, result.getRating());
        verify(validationService, times(1)).validateRating(any(Double.class));
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(movieMapper, times(1)).toDto(testMovie);
    }

    @Test
    void createMovie_WithNonExistingGenre_ShouldThrowException() {
        doNothing().when(validationService).validateRating(any(Double.class));
        when(genreRepository.findById(999L)).thenReturn(Optional.empty());
        testMovieDto.setGenreId(999L);

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.createMovie(testMovieDto);
        });

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void createMovie_WithInvalidRating_ShouldThrowException() {
        testMovieDto.setRating(11.0);
        doThrow(new IllegalArgumentException("Rating must be between 0.0 and 10.0"))
                .when(validationService).validateRating(11.0);

        assertThrows(IllegalArgumentException.class, () -> {
            movieService.createMovie(testMovieDto);
        });

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void findAllMovies_ShouldReturnListOfMovies() {
        Movie movie2 = Movie.builder()
                .id(2L)
                .title("Inception")
                .director("Nolan")
                .rating(8.8)
                .genre(testGenre)
                .build();

        MovieDto movieDto2 = MovieDto.builder()
                .id(2L)
                .title("Inception")
                .director("Nolan")
                .rating(8.8)
                .genreId(1L)
                .genreName("Action")
                .build();

        when(movieRepository.findAll()).thenReturn(Arrays.asList(testMovie, movie2));
        when(movieMapper.toDto(testMovie)).thenReturn(testMovieDto);
        when(movieMapper.toDto(movie2)).thenReturn(movieDto2);

        List<MovieDto> result = movieService.findAllMovies();

        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
        verify(movieMapper, times(2)).toDto(any(Movie.class));
    }

    @Test
    void findMovieById_WithExistingId_ShouldReturnMovieDto() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(movieMapper.toDto(testMovie)).thenReturn(testMovieDto);

        MovieDto result = movieService.findMovieById(1L);

        assertNotNull(result);
        assertEquals("The Matrix", result.getTitle());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieMapper, times(1)).toDto(testMovie);
    }

    @Test
    void findMovieById_WithNonExistingId_ShouldThrowException() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.findMovieById(999L);
        });
    }

    @Test
    void findMoviesByGenre_WithExistingGenre_ShouldReturnMovies() {
        when(genreRepository.existsById(1L)).thenReturn(true);
        when(movieRepository.findByGenreId(1L)).thenReturn(Arrays.asList(testMovie));
        when(movieMapper.toDto(testMovie)).thenReturn(testMovieDto);

        List<MovieDto> result = movieService.findMoviesByGenre(1L);

        assertEquals(1, result.size());
        assertEquals("The Matrix", result.get(0).getTitle());
        verify(movieMapper, times(1)).toDto(testMovie);
    }

    @Test
    void updateMovie_WithValidData_ShouldReturnUpdatedMovie() {
        MovieDto updateDto = MovieDto.builder()
                .title("The Matrix Reloaded")
                .director("Wachowski")
                .rating(7.2)
                .genreId(1L)
                .build();

        doNothing().when(validationService).validateRating(any(Double.class));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(testMovie));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(testGenre));
        doNothing().when(movieMapper).updateEntityFromDto(updateDto, testMovie, testGenre);
        when(movieRepository.save(testMovie)).thenReturn(testMovie);
        when(movieMapper.toDto(testMovie)).thenReturn(updateDto);

        MovieDto result = movieService.updateMovie(1L, updateDto);

        assertNotNull(result);
        verify(movieMapper, times(1)).updateEntityFromDto(updateDto, testMovie, testGenre);
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    void deleteMovie_WithExistingId_ShouldDeleteMovie() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        movieService.deleteMovie(1L);

        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMovie_WithNonExistingId_ShouldThrowException() {
        when(movieRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.deleteMovie(999L);
        });
    }
}
