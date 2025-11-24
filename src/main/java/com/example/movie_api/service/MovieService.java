package com.example.movie_api.service;

import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.repository.MovieRepository;
import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.mapper.MovieMapper;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final MovieMapper movieMapper;
    private final ValidationService validationService;

    @Transactional
    public MovieDto createMovie(MovieDto movieDto) {
        validationService.validateRating(movieDto.getRating());

        Genre genre = genreRepository.findById(movieDto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre", movieDto.getGenreId()));

        Movie movie = movieMapper.toEntity(movieDto, genre);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findAllMovies() {
        return movieRepository.findAll().stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDto findMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));
        return movieMapper.toDto(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> findMoviesByGenre(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            throw new ResourceNotFoundException("Genre", genreId);
        }

        return movieRepository.findByGenreId(genreId).stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        validationService.validateRating(movieDto.getRating());

        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", id));

        Genre genre = genreRepository.findById(movieDto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre", movieDto.getGenreId()));

        movieMapper.updateEntityFromDto(movieDto, existingMovie, genre);
        Movie updatedMovie = movieRepository.save(existingMovie);
        return movieMapper.toDto(updatedMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie", id);
        }
        movieRepository.deleteById(id);
    }
}
