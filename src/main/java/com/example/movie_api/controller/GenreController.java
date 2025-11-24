package com.example.movie_api.controller;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.service.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base-path}/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDto> createGenre(@Valid @RequestBody GenreDto genreDto) {
        GenreDto createdGenre = genreService.createGenre(genreDto);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> genres = genreService.findAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDto> getGenreById(@PathVariable Long id) {
        GenreDto genre = genreService.findGenreById(id);
        return ResponseEntity.ok(genre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDto> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody GenreDto genreDto) {
        GenreDto updatedGenre = genreService.updateGenre(id, genreDto);
        return ResponseEntity.ok(updatedGenre);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
