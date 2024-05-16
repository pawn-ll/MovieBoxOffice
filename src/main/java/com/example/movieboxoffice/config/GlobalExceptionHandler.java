package com.example.movieboxoffice.config;

import com.example.movieboxoffice.entity.ConditionException;
import com.example.movieboxoffice.entity.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception ex) {;
        return Response.error(ex.getMessage());
    }

    @ExceptionHandler(ConditionException.class)
    @ResponseBody
    public Response handleException(ConditionException ex) {;
        return Response.error(ex.getMessage());
    }


}

