package com.example.movie_api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {

    private Long id;

    @NotBlank(message = "Movie title cannot be blank")
    private String title;

    @NotBlank(message = "Director name cannot be blank")
    private String director;

    @NotNull(message = "Rating cannot be null")
    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must not exceed 10.0")
    private Double rating;

    @NotNull(message = "Genre ID cannot be null")
    private Long genreId;

    private String genreName;
}