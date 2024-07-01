package com.example.CaseStudy.Mappers;

import com.example.CaseStudy.Entities.Movie;
import com.example.CaseStudy.Models.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieDTO toMovieDTO(Movie movie);
    Movie toMovie(MovieDTO movieDTO);
    
    void updateMovieFromDto(MovieDTO movieDTO, @MappingTarget Movie movie);
}
