package com.example.Book.Store.Application.mapper;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<Book, BookDTO>{
    @Override
    public Book mapToEntity(BookDTO bookDTO) {
        return Book.builder()
                .bookTitle(bookDTO.bookTitle())
                .bookAuthor(bookDTO.bookAuthor())
                .bookYear(bookDTO.bookYear())
                .build();
    }

    @Override
    public BookDTO mapToDTO(Book bookStore) {
        return BookDTO.builder()
                .id(bookStore.getId())
                .bookAuthor(bookStore.getBookAuthor())
                .bookTitle(bookStore.getBookTitle())
                .bookYear(bookStore.getBookYear())
                .build();
    }
}
