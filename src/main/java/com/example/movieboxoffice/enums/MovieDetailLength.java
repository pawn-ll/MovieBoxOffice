package com.example.movieboxoffice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MovieDetailLength {

    DIRECTOR(64, "director"),
    SCIPTER(256, "scripter"),
    ACTOR(256, "actor"),
    TYPE(64, "type"),
    RELEASE_DATE(128, "releaseDate"),
    LENGTH(64, "length"),
    AREA(64, "area"),
    INTRODUCTION(1024, "introduction"),
    POSTER(1024, "poster");



    private Integer length;
    private String fileName;

}
