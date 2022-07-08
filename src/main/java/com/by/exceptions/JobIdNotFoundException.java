package com.by.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobIdNotFoundException extends RuntimeException{
    public JobIdNotFoundException(String message){
        super(message);
    }
}
