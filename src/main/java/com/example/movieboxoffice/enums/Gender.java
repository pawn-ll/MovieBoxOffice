package com.example.movieboxoffice.enums;

public enum Gender {

    FEMALE(0, "女"),

    MALE(1, "男"),

    UNKNOWN(2, "未知");

    private Integer gender;

    private String genderName;

    Gender(Integer gender, String genderName) {
        this.gender = gender;
        this.genderName = genderName;
    }

    public Integer getGender() {
        return gender;
    }

    public String getGenderName() {
        return genderName;
    }

}
