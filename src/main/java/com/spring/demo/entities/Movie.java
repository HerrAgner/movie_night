package com.spring.demo.entities;

import org.springframework.data.annotation.Id;

public class Movie {

    @Id
    private String id ;
    private String title;
    private float imdbRating;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public Movie() {
    }

    public Movie(String title, float imdbRating) {
        this.title = title;
        this.imdbRating = imdbRating;
    }

}
