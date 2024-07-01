package com.example.CaseStudy.Models;

public class MovieDTO {

    private String title;
    private String director;
    private int year;

    public MovieDTO() {
    }

    public MovieDTO(String title, String director, int year) {
        this.title = title;
        this.director = director;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "MovieDTO{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                '}';
    }
}
