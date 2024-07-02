package com.example.CaseStudy.Controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Mappers.MovieMapper;
import com.example.CaseStudy.Models.MovieDTO;
import com.example.CaseStudy.Repositories.MovieRepository;

@RestController
@RequestMapping("/api")
public class MovieController {

    private final MovieRepository repository;
    private final MovieMapper mapper;

    @Autowired
    MovieController(MovieRepository repository, MovieMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    //tested
    @GetMapping("/countAll")
    public ResponseEntity<Object> countAllMovies() {
        Long count = repository.count();
        return ResponseEntity.ok().body("{\"count\": " + count + "}");
    }

    @GetMapping("/movies")
    public ResponseEntity<List<EntityModel<Movie>>> getAllMovies() {
        List<Movie> movies = repository.findAll();
        List<EntityModel<Movie>> movieModels = movies.stream()
                .map(movie -> EntityModel.of(movie,
                        Link.of("/api/movies/" + movie.getId()).withSelfRel(),
                        Link.of("/api/movies/" + movie.getId() + "/details").withRel("details")))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(movieModels);
    }
   
    @GetMapping("/movies/{id}")
    public ResponseEntity<EntityModel<Movie>> getMovieById(@PathVariable Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        EntityModel<Movie> movieModel = EntityModel.of(movie,
                Link.of("/api/movies/" + id).withSelfRel(),
                Link.of("/api/movies/" + id + "/details").withRel("details"));

        return ResponseEntity.ok().body(movieModel);
    }

    @GetMapping("/movies/{id}/details")
    public ResponseEntity<Movie> getMovieDetails(@PathVariable Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id: " + id + " not found"));

        return ResponseEntity.ok().body(movie);
    }

    //tested
    @PostMapping("/addMovie")
    public ResponseEntity<Movie> addMovie(@RequestBody MovieDTO movieDTO) {
        Movie newMovie = mapper.toMovie(movieDTO);
        Movie savedMovie = repository.save(newMovie);
        return ResponseEntity.ok().body(savedMovie);
    }

    @DeleteMapping("/deleteMovie/{id}")
    public ResponseEntity<Movie> deleteMovie(@PathVariable Long id) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id: " + id + " not found"));
        repository.delete(movie);
        return ResponseEntity.ok().body(movie);
    }

    @PutMapping("/updateMovie/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody MovieDTO movieDTO) {
        Movie movie = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie with id: " + id + " not found"));
        mapper.updateMovieFromDto(movieDTO, movie);
        Movie updatedMovie = repository.save(movie);
        return ResponseEntity.ok().body(updatedMovie);    
    }
}
