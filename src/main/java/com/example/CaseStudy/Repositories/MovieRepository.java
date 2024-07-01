package com.example.CaseStudy.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.CaseStudy.Entities.Movie;

public interface MovieRepository  extends JpaRepository<Movie, Long> {
    public long count();
}
