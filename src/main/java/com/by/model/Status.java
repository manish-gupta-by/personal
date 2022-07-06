package com.by.model;

public enum Status {
    CREATED("created"),
    EXISTS("Already Exists");

    String value;
    Status(String value){
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
