package com.example.CaseStudy.Controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Repositories.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.CaseStudy.Models.MovieDTO;
import com.example.CaseStudy.Mappers.MovieMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository repository;

    @MockBean
    private MovieMapper mapper;


    @InjectMocks
    private MovieController controller;

    // @BeforeEach
    // public void setUp() {
    //     MockitoAnnotations.openMocks(this);
    // }

    @BeforeEach
    public void SetUp() {
        repository.deleteAll();
        // repository.flush();
    }

    @Test
    public void testCountAllMovies() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDirector("Test Director");
        movie.setYear(2021);
        when(repository.count()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/countAll"))
               .andExpect(status().isOk())
               .andExpect(content().json("{\"count\": 1}"));
    }

    // @Test
    // public void testCountAllMovies() throws Exception {
    //     Movie movie = new Movie();
    //     movie.setTitle("Test Movie");
    //     movie.setDirector("Test Director");
    //     movie.setYear(2021);
    //     repository.save(movie); // Salva il film nel database H2

    //     mockMvc.perform(MockMvcRequestBuilders.get("/api/countAll"))
    //             .andDo(print())
    //            .andExpect(status().isOk())
    //            .andExpect(content().json("{\"count\": 1}"));
    // }

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDirector("Test Director");
        movie.setYear(2021);

        when(repository.findAll()).thenReturn(Collections.singletonList(movie));

        EntityModel<Movie> movieModel = EntityModel.of(movie,
                Link.of("/api/movies/" + movie.getId()).withRel("self"),
                Link.of("/api/movies/" + movie.getId() + "/details").withRel("details"));

        
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].title").value("Test Movie"))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].director").value("Test Director"))
            .andExpect(jsonPath("$[0].year").value(2021))
            .andExpect(jsonPath("$[0].links[0].rel").value("self"))
            .andExpect(jsonPath("$[0].links[0].href").value("/api/movies/1"))
            .andExpect(jsonPath("$[0].links[1].rel").value("details"))
            .andExpect(jsonPath("$[0].links[1].href").value("/api/movies/1/details"));
    }

    @Test
    public void testGetMovieById() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDirector("Test Director");
        movie.setYear(2021);

        EntityModel<Movie> movieModel = EntityModel.of(movie,
                Link.of("/api/movies/" + movie.getId()).withRel("self"),
                Link.of("/api/movies/" + movie.getId() + "/details").withRel("details"));


        when(repository.findById(1L)).thenReturn(java.util.Optional.of(movie));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{id}", 1L))
            .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("Test Movie"))
           .andExpect(jsonPath("$.director").value("Test Director"))
           .andExpect(jsonPath("$.year").value(2021))
           .andExpect(jsonPath("$._links.self.href").value("/api/movies/1"))
           .andExpect(jsonPath("$._links.details.href").value("/api/movies/1/details"));
    }

    @Test
    public void testGetMovieDetails() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDirector("Test Director");
        movie.setYear(2021);

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(movie));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{id}/details", 1L))
                .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Test Movie"))
               .andExpect(jsonPath("$.director").value("Test Director"))
               .andExpect(jsonPath("$.year").value(2021));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie to delete");
        movie.setDirector("Test Director to delete");
        movie.setYear(2021);

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(movie));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/deleteMovie/{id}", 1L))
                .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title").value("Test Movie to delete"))
               .andExpect(jsonPath("$.director").value("Test Director to delete"))
               .andExpect(jsonPath("$.year").value(2021));
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("New Movie Added");
        movieDTO.setDirector("New Director Added");
        movieDTO.setYear(2020);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("New Movie Added");
        movie.setDirector("New Director Added");
        movie.setYear(2020);

        when(mapper.toMovie(any(MovieDTO.class))).thenReturn(movie);

        when(repository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movieDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Movie Added"))
                .andExpect(jsonPath("$.director").value("New Director Added"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Movie Updated");
        movieDTO.setDirector("Director Updated");
        movieDTO.setYear(2020);

        Movie oldMovie = new Movie();
        oldMovie.setId(1L);
        oldMovie.setTitle("Old Movie");
        oldMovie.setDirector("Old Director");
        oldMovie.setYear(2019);

        Movie newMovie = new Movie();
        newMovie.setId(1L);
        newMovie.setTitle("Movie Updated");
        newMovie.setDirector("Director Updated");
        newMovie.setYear(2020);

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(oldMovie));

        doAnswer(invocation -> {
        Object[] args = invocation.getArguments();
        MovieDTO dto = (MovieDTO) args[0];
        Movie movie = (Movie) args[1];
        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setYear(dto.getYear());
        return null; 
        }).when(mapper).updateMovieFromDto(any(MovieDTO.class), any(Movie.class));

        when(repository.save(any(Movie.class))).thenReturn(newMovie);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateMovie/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(movieDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Movie Updated"))
                .andExpect(jsonPath("$.director").value("Director Updated"))
                .andExpect(jsonPath("$.year").value(2020));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
