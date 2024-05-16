package com.example.movieboxoffice.entity;

import lombok.Data;

@Data
public class ConditionException extends Exception{


    public ConditionException(String message) {
        super(message);
    }
}
