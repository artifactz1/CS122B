package com.github.klefstad_teaching.cs122b.movies.data;

public class Genre {

    private Long genreId;
    private String name;

    public Long getGenreId() {
        return genreId;
    }

    public Genre setGenreId(Long genreId) {
        this.genreId = genreId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Genre setName(String name) {
        this.name = name;
        return this;
    }

}