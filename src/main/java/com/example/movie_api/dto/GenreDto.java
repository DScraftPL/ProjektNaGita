package com.example.movie_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDto {

    private Long id;

    @NotBlank(message = "Genre name cannot be blank")
    private String name;

    @NotNull(message = "Follower count cannot be null")
    @Min(value = 0, message = "Follower count must be non-negative")
    private Integer followerCount;
}