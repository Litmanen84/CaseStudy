package com.example.CaseStudy.Controllers;

import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Models.MovieDTO;
import com.example.CaseStudy.Repositories.MovieRepository;
import com.example.CaseStudy.Mappers.MovieMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository repository;

    @MockBean
    private MovieMapper mapper;

    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDirector("Test Director");
        movie.setYear(2021);

        when(repository.findAll()).thenReturn(Collections.singletonList(movie));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"));
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("New Movie");
        movieDTO.setDirector("New Director");
        movieDTO.setYear(2022);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("New Movie");
        movie.setDirector("New Director");
        movie.setYear(2022);

        when(mapper.toMovie(any(MovieDTO.class))).thenReturn(movie);
        when(repository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/addMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"New Movie\", \"director\": \"New Director\", \"year\": 2022}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Movie"));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Updated Movie after test");
        movieDTO.setDirector("Updated Director after test");
        movieDTO.setYear(2023);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Movie to update");
        movie.setDirector("Director to Update");
        movie.setYear(2023);

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(movie));
        doNothing().when(mapper).updateMovieFromDto(any(MovieDTO.class), any(Movie.class));
        when(repository.save(any(Movie.class))).thenReturn(movie);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateMovie/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Movie after test\", \"director\": \"Updated Director after test\", \"year\": 2023}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Movie after test"))
                .andExpect(jsonPath("$.director").value("Updated Director after test"))
                .andExpect(jsonPath("$.year").value(2023));    
    }
}
