package com.example.CaseStudy.Controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Collections;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Repositories.MovieRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieRepository repository;

    @InjectMocks
    private MovieController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
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

    @Test
public void testGetAllMovies() throws Exception {
    Movie movie = new Movie();
    movie.setId(1L);
    movie.setTitle("Test Movie");
    movie.setDirector("Test Director");
    movie.setYear(2021);

    EntityModel<Movie> movieModel = EntityModel.of(movie,
            Link.of("/api/movies/" + movie.getId()).withRel("self"),
            Link.of("/api/movies/" + movie.getId() + "/details").withRel("details"));

    when(repository.findAll()).thenReturn(Collections.singletonList(movie));

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
}


// package com.example.CaseStudy.Controllers;

// import com.example.CaseStudy.Entities.Movie;
// import com.example.CaseStudy.Repositories.MovieRepository;
// import com.example.CaseStudy.Mappers.MovieMapper;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.http.MediaType;
// import java.util.Collections;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest(MovieController.class)
// @ExtendWith(MockitoExtension.class)
// public class MovieControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private MovieRepository repository;

//     @MockBean
//     private MovieMapper mapper;

//     @Test
//     public void testGetAllMovies() throws Exception {
//         Movie movie = new Movie();
//         movie.setId(1L);
//         movie.setTitle("Test Movie");
//         movie.setDirector("Test Director");
//         movie.setYear(2021);

//         when(repository.findAll()).thenReturn(Collections.singletonList(movie));

//         mockMvc.perform(MockMvcRequestBuilders.get("/api/movies")
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].title").value("Test Movie"));
//     }

//     @Test
//     public void testCountAllMovies() throws Exception {
//         Movie movie = new Movie();
//         movie.setId(1L);
//         movie.setTitle("Test Movie");
//         movie.setDirector("Test Director");
//         movie.setYear(2021);

//         when(repository.count()).thenReturn(1L);
//         mockMvc.perform(MockMvcRequestBuilders.get("/api/countAll"))
//                 .andExpect(status().isOk())
//                 .andExpect(content().json("{\"count\": 1}"));
//     }

//     @Test
//     public void testGetMovieById() throws Exception {
//     Movie movie = new Movie();
//     movie.setId(1L);
//     movie.setTitle("Test Movie");
//     movie.setDirector("Test Director");
//     movie.setYear(2021);

//     // Mock del repository per ritornare il movie di esempio quando findById viene chiamato con l'ID 1L
//     when(repository.findById(1L)).thenReturn(java.util.Optional.of(movie));

//     // Esecuzione della richiesta GET /api/movies/{id} e verifica della risposta
//     mockMvc.perform(MockMvcRequestBuilders.get("/api/movies/{id}", 1L)
//             .accept(MediaType.APPLICATION_JSON))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.title").value("Test Movie"))
//             .andExpect(jsonPath("$.director").value("Test Director"))
//             .andExpect(jsonPath("$.year").value(2021));
//     }

//     // @Test
//     // public void testAddMovie() throws Exception {
//     //     MovieDTO movieDTO = new MovieDTO();
//     //     movieDTO.setTitle("New Movie");
//     //     movieDTO.setDirector("New Director");
//     //     movieDTO.setYear(2022);

//     //     Movie movie = new Movie();
//     //     movie.setId(1L);
//     //     movie.setTitle("New Movie");
//     //     movie.setDirector("New Director");
//     //     movie.setYear(2022);

//     //     when(mapper.toMovie(any(MovieDTO.class))).thenReturn(movie);
//     //     when(repository.save(any(Movie.class))).thenReturn(movie);

//     //     mockMvc.perform(MockMvcRequestBuilders.post("/api/addMovie")
//     //             .contentType(MediaType.APPLICATION_JSON)
//     //             .content("{\"title\": \"New Movie\", \"director\": \"New Director\", \"year\": 2022}"))
//     //             .andExpect(status().isOk())
//     //             .andExpect(jsonPath("$.title").value("New Movie"));
//     // }
// }
