package com.example.movie_api.controller;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.model.Genre;
import com.example.movie_api.repository.GenreRepository;
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
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
    }

    @Test
    void createGenre_WithValidData_ShouldReturnCreated() throws Exception {
        GenreDto genreDto = GenreDto.builder()
                .name("Action")
                .followerCount(1000)
                .build();

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Action"))
                .andExpect(jsonPath("$.followerCount").value(1000))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void createGenre_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        GenreDto genreDto = GenreDto.builder()
                .name("")
                .followerCount(-1)
                .build();

        mockMvc.perform(post("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(genreDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllGenres_ShouldReturnListOfGenres() throws Exception {
        Genre genre1 = Genre.builder().name("Action").followerCount(1000).build();
        Genre genre2 = Genre.builder().name("Drama").followerCount(800).build();
        genreRepository.save(genre1);
        genreRepository.save(genre2);

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Action"))
                .andExpect(jsonPath("$[1].name").value("Drama"));
    }

    @Test
    void getGenreById_WithExistingId_ShouldReturnGenre() throws Exception {
        Genre genre = Genre.builder().name("Action").followerCount(1000).build();
        Genre savedGenre = genreRepository.save(genre);

        mockMvc.perform(get("/api/genres/" + savedGenre.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action"))
                .andExpect(jsonPath("$.followerCount").value(1000));
    }

    @Test
    void getGenreById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/genres/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGenre_WithValidData_ShouldReturnUpdatedGenre() throws Exception {
        Genre genre = Genre.builder().name("Action").followerCount(1000).build();
        Genre savedGenre = genreRepository.save(genre);

        GenreDto updateDto = GenreDto.builder()
                .name("Action/Adventure")
                .followerCount(1500)
                .build();

        mockMvc.perform(put("/api/genres/" + savedGenre.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Action/Adventure"))
                .andExpect(jsonPath("$.followerCount").value(1500));
    }

    @Test
    void deleteGenre_WithExistingId_ShouldReturnNoContent() throws Exception {
        Genre genre = Genre.builder().name("Action").followerCount(1000).build();
        Genre savedGenre = genreRepository.save(genre);

        mockMvc.perform(delete("/api/genres/" + savedGenre.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/genres/" + savedGenre.getId()))
                .andExpect(status().isNotFound());
    }
}

