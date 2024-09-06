package com.example.Book.Store.Application.mapperTest;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.mapper.BookMapper;
import com.example.Book.Store.Application.mock.MockBook;
import com.example.Book.Store.Application.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookMapperTest {
    private final BookMapper bookMapper = new BookMapper();

    @Test
    @DisplayName("Given: BookDTO, When: mapToEntity, Then: return Book")
    void mapToEntity() {
        Book book = MockBook.mockBook();
        BookDTO bookDTO = MockBook.mockBookDTO();

        assertThat(bookMapper.mapToEntity(bookDTO)).isEqualTo(book);
    }

    @Test
    @DisplayName("Given: Book, When: mapToDTO, Then: return BookDTO")
    void mapToDTO() {
        Book book = MockBook.mockBook();
        BookDTO bookDTO = MockBook.mockBookDTO();

        assertThat(bookMapper.mapToDTO(book)).isEqualTo(bookDTO);
    }
}
