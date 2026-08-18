package com.example.library.exception;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Illegal Argument

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(

            IllegalArgumentException ex,

            Model model) {

        model.addAttribute(
                "errorMessage",
                ex.getMessage()
        );

        return "error";

    }

    // Entity Not Found

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(

            EntityNotFoundException ex,

            Model model) {

        model.addAttribute(
                "errorMessage",
                ex.getMessage()
        );

        return "error";

    }

    // General Exception

    @ExceptionHandler(Exception.class)
    public String handleException(

            Exception ex,

            Model model) {

        model.addAttribute(
                "errorMessage",
                "Something went wrong. Please try again."
        );

        return "error";

    }

}