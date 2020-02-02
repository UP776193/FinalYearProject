package com.example.FinalYearProject;

public class MissingBlockException extends Exception{

    public MissingBlockException(String message) {
        super(message);
        System.out.println("CRASH: Block not found");
    }
}
