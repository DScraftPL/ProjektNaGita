package com.example.movie_api.mapper;

import com.example.movie_api.dto.GenreDto;
import com.example.movie_api.model.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapperImpl implements GenreMapper {

    @Override
    public GenreDto toDto(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Cannot convert null Genre to DTO");
        }

        return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .followerCount(genre.getFollowerCount())
                .build();
    }

    @Override
    public Genre toEntity(GenreDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot convert null GenreDto to entity");
        }

        return Genre.builder()
                .id(dto.getId())
                .name(dto.getName())
                .followerCount(dto.getFollowerCount())
                .build();
    }

    @Override
    public void updateEntityFromDto(GenreDto dto, Genre entity) {
        if (dto == null) {
            throw new IllegalArgumentException("Cannot update entity from null DTO");
        }

        if (entity == null) {
            throw new IllegalArgumentException("Cannot update null entity");
        }

        entity.setName(dto.getName());
        entity.setFollowerCount(dto.getFollowerCount());
    }
}
