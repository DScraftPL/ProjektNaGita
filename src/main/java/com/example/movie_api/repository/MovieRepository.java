package com.example.movie_api.repository;

import com.example.movie_api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenreId(Long genreId);
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByDirectorContainingIgnoreCase(String director);
}
