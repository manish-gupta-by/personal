package com.by;

import com.by.exceptions.JobIdNotFoundException;
import com.by.exceptions.NoEmployeeWithSkillFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.ws.Response;

//@RestControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler({JobIdNotFoundException.class, NoEmployeeWithSkillFound.class})
    ResponseEntity notFoundExceptions(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity allExceptions(Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
}
