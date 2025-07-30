package com.demo.jooq.controller;

import com.demo.jooq.model.Movie;
import com.demo.jooq.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // C - Create
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.createMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    // R - Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
        return movieService.getMovieById(id)
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // R - Get by year
    @GetMapping("/year/{year}")
    public ResponseEntity<List<Movie>> getMoviesByYear(@PathVariable Integer year) {
        List<Movie> movies = movieService.getMoviesByYear(year);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // R - Get by year in
    @GetMapping("/year")
    public ResponseEntity<List<Movie>> getMoviesByYearIn(@RequestParam Set<Integer> years) {
        List<Movie> movies = movieService.getMoviesByYearIn(years);
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // R - Get all by pagination
    @GetMapping("/paged")
    public ResponseEntity<List<Movie>> getAllMoviesPaged(
            @RequestParam(defaultValue = "0") int page, // 默认页码从0开始
            @RequestParam(defaultValue = "1") int size) { // 默认每页1条
        List<Movie> movies = movieService.getAllMoviesPaged(page, size);
        if (movies.isEmpty() && page > 0) {
            // 如果查询到空页且不是第一页
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // R - Get all
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // U - Update
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Integer id, @RequestBody Movie movieDetails) {
        return movieService.updateMovie(id, movieDetails)
                .map(updatedMovie -> new ResponseEntity<>(updatedMovie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // U - Increment likes
    @PatchMapping("/{id}/like")
    public ResponseEntity<Movie> likeMovie(@PathVariable Integer id) {
        return movieService.incrementMovieLikes(id)
                .map(movie -> new ResponseEntity<>(movie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // D - Delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
    }

    // D - Delete all
    @DeleteMapping("/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllMovies() {
        movieService.deleteAllMovies();
    }
}