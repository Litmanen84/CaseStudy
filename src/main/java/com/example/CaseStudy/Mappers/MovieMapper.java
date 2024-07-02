package com.example.CaseStudy.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Models.MovieDTO;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO toMovieDTO(Movie movie);
    Movie toMovie(MovieDTO movieDTO);
    
    void updateMovieFromDto(MovieDTO movieDTO, @MappingTarget Movie movie);
}
