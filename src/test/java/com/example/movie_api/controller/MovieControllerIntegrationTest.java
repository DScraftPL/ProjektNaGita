package com.example.movie_api.controller;

import com.example.movie_api.dto.MovieDto;
import com.example.movie_api.model.Genre;
import com.example.movie_api.model.Movie;
import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Genre testGenre;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        genreRepository.deleteAll();

        testGenre = Genre.builder()
                .name("Action")
                .followerCount(1000)
                .build();
        testGenre = genreRepository.save(testGenre);
    }

    @Test
    void createMovie_WithValidData_ShouldReturnCreated() throws Exception {
        MovieDto movieDto = MovieDto.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genreId(testGenre.getId())
                .build();

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Matrix"))
                .andExpect(jsonPath("$.director").value("Wachowski"))
                .andExpect(jsonPath("$.rating").value(8.7))
                .andExpect(jsonPath("$.genreName").value("Action"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createMovie_WithInvalidRating_ShouldReturnBadRequest() throws Exception {
        MovieDto movieDto = MovieDto.builder()
                .title("Test Movie")
                .director("Test Director")
                .rating(15.0)
                .genreId(testGenre.getId())
                .build();

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMovie_WithNonExistingGenre_ShouldReturnNotFound() throws Exception {
        MovieDto movieDto = MovieDto.builder()
                .title("Test Movie")
                .director("Test Director")
                .rating(8.0)
                .genreId(999L)
                .build();

        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllMovies_ShouldReturnListOfMovies() throws Exception {
        Movie movie1 = Movie.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();

        Movie movie2 = Movie.builder()
                .title("Die Hard")
                .director("McTiernan")
                .rating(8.2)
                .genre(testGenre)
                .build();

        movieRepository.save(movie1);
        movieRepository.save(movie2);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("The Matrix"))
                .andExpect(jsonPath("$[1].title").value("Die Hard"));
    }

    @Test
    void getMovieById_WithExistingId_ShouldReturnMovie() throws Exception {
        Movie movie = Movie.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();
        Movie savedMovie = movieRepository.save(movie);

        mockMvc.perform(get("/api/movies/" + savedMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix"))
                .andExpect(jsonPath("$.director").value("Wachowski"))
                .andExpect(jsonPath("$.rating").value(8.7));
    }

    @Test
    void getMovieById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMoviesByGenre_ShouldReturnMoviesOfGenre() throws Exception {
        Movie movie = Movie.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();
        movieRepository.save(movie);

        mockMvc.perform(get("/api/movies/genre/" + testGenre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    void updateMovie_WithValidData_ShouldReturnUpdatedMovie() throws Exception {
        Movie movie = Movie.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();
        Movie savedMovie = movieRepository.save(movie);

        MovieDto updateDto = MovieDto.builder()
                .title("The Matrix Reloaded")
                .director("Wachowski Sisters")
                .rating(7.2)
                .genreId(testGenre.getId())
                .build();

        mockMvc.perform(put("/api/movies/" + savedMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("The Matrix Reloaded"))
                .andExpect(jsonPath("$.director").value("Wachowski Sisters"))
                .andExpect(jsonPath("$.rating").value(7.2));
    }

    @Test
    void deleteMovie_WithExistingId_ShouldReturnNoContent() throws Exception {
        Movie movie = Movie.builder()
                .title("The Matrix")
                .director("Wachowski")
                .rating(8.7)
                .genre(testGenre)
                .build();
        Movie savedMovie = movieRepository.save(movie);

        mockMvc.perform(delete("/api/movies/" + savedMovie.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/movies/" + savedMovie.getId()))
                .andExpect(status().isNotFound());
    }
}
