package com.by.exceptions;

public class NoEmployeeWithSkillFound extends RuntimeException{

    public NoEmployeeWithSkillFound(String message){
        super(message);
    }
}
