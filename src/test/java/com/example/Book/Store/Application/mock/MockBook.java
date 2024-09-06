package com.example.Book.Store.Application.mock;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.model.Book;

public class MockBook {
    public static BookDTO mockBookDTO() {
        return mockBookDTO(null, "Title", "Author", 2024);
    }

    public static BookDTO mockBookDTO(Integer id, String title, String author, Integer year) {
        return BookDTO.builder()
                .id(id)
                .bookTitle(title)
                .bookAuthor(author)
                .bookYear(year)
                .build();
    }

    public static Book mockBook() {
        return mockBook(null);
    }

    public static Book mockBook(Integer id) {
        return Book.builder()
                .id(id)
                .bookTitle("Title")
                .bookAuthor("Author")
                .bookYear(2024)
                .build();
    }
}
