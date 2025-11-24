package com.example.movie_api.service;

import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.exception.ResourceNotFoundException;
import com.example.movie_api.mapper.GenreMapper;
import com.example.movie_api.model.Genre;
import com.example.movie_api.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final ValidationService validationService;

    @Transactional
    public GenreDto createGenre(GenreDto genreDto) {
        validationService.validateGenreName(genreDto.getName());
        validationService.validateFollowerCount(genreDto.getFollowerCount());

        if (genreRepository.existsByName(genreDto.getName())) {
            throw new IllegalArgumentException("Genre with name '" + genreDto.getName() + "' already exists");
        }

        Genre genre = genreMapper.toEntity(genreDto);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDto(savedGenre);
    }

    @Transactional(readOnly = true)
    public List<GenreDto> findAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GenreDto findGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        return genreMapper.toDto(genre);
    }

    @Transactional
    public GenreDto updateGenre(Long id, GenreDto genreDto) {
        validationService.validateGenreName(genreDto.getName());
        validationService.validateFollowerCount(genreDto.getFollowerCount());

        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));

        if (!existingGenre.getName().equals(genreDto.getName())
                && genreRepository.existsByName(genreDto.getName())) {
            throw new IllegalArgumentException("Genre with name '" + genreDto.getName() + "' already exists");
        }

        genreMapper.updateEntityFromDto(genreDto, existingGenre);
        Genre updatedGenre = genreRepository.save(existingGenre);
        return genreMapper.toDto(updatedGenre);
    }

    @Transactional
    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", id);
        }
        genreRepository.deleteById(id);
    }
}
