package com.example.Book.Store.Application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookDTO(
        Integer id,
        @NotBlank(message = "BookTitle is required") String bookTitle,
        @NotBlank(message = "Book Author is required") String bookAuthor,
        @NotNull(message = "Book Year is required") Integer bookYear
) {
}
