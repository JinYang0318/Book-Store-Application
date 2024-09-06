package com.example.Book.Store.Application.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message){
        super(message);
    }
}
