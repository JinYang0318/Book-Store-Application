package com.example.Book.Store.Application.service;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.mapper.BookMapper;
import com.example.Book.Store.Application.mapper.Mapper;
import com.example.Book.Store.Application.mock.MockBook;
import com.example.Book.Store.Application.model.Book;
import com.example.Book.Store.Application.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BookService.class, BookMapper.class})
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @SpyBean
    private Mapper<Book, BookDTO> mapper;

    @Test
    @DisplayName("Given: id valid, When: getBookById, Then: return bookDTO")
    void getBookById() {
        BookDTO expectedBookDTO = MockBook.mockBookDTO(1, "Title", "Author", 2024);
        when(bookRepository.findById(anyInt()))
                .thenReturn(Optional.of(MockBook.mockBook(1)));

        Optional<BookDTO> resultBookDTO = bookService.getBookById(1);

        verify(bookRepository).findById(1);
        verify(mapper).mapToDTO(any(Book.class));
        assertThat(resultBookDTO).isEqualTo(Optional.of(expectedBookDTO));
    }

    @Test
    @DisplayName("Given: id not found, When: getBookById, Then: return optional empty")
    void getBookByIdNotFound() {
        when(bookRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        Optional<BookDTO> resultBookDTO = bookService.getBookById(999);

        verify(bookRepository).findById(999);
        verify(mapper, never()).mapToDTO(any(Book.class));
        assertThat(resultBookDTO).isEmpty();
    }

    @Test
    @DisplayName("Given: -, When: getAllBooks, Then: return bookDTO")
    void getAllBooks() {
        List<BookDTO> expectedBookDTOList = List.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookRepository.findAll())
                .thenReturn(List.of(MockBook.mockBook(1)));

        List<BookDTO> resultBookDTOList = bookService.getAllBooks();

        verify(bookRepository).findAll();
        verify(mapper).mapToDTO(any(Book.class));
        assertThat(resultBookDTOList).isEqualTo(expectedBookDTOList);
    }

    @Test
    @DisplayName("Given: ids valid, When: getBookByIds, Then: return bookDTO list")
    void getBookByIds() {
        List<BookDTO> expectedBookDTOList = List.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookRepository.findAllById(anyList()))
                .thenReturn(List.of(MockBook.mockBook(1)));

        List<BookDTO> resultBookDTOList = bookService.getBookByIds(List.of(1));

        verify(bookRepository).findAllById(List.of(1));
        verify(mapper).mapToDTO(any(Book.class));
        assertThat(resultBookDTOList).isEqualTo(expectedBookDTOList);
    }

    @Test
    @DisplayName("Given: ids not found, When: getBookByIds, Then: return empty bookDTO list")
    void getBookByIdsNotFound() {
        when(bookRepository.findAllById(anyList()))
                .thenReturn(Collections.emptyList());

        List<BookDTO> resultBookDTOList = bookService.getBookByIds(List.of(999));

        verify(bookRepository).findAllById(List.of(999));
        verify(mapper, never()).mapToDTO(any(Book.class));
        assertThat(resultBookDTOList).isEmpty();
    }

    @Test
    @DisplayName("Given: book, When: createBook, Then: return bookDTO")
    void createBook() {
        Optional<BookDTO> expectedBookDTO =
                Optional.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookRepository.save(any(Book.class))).thenReturn(MockBook.mockBook(1));

        Optional<BookDTO> resultBookDTO = bookService.createBook(expectedBookDTO.get());

        verify(bookRepository).save(any(Book.class));
        verify(mapper).mapToDTO(any(Book.class));
        assertThat(resultBookDTO).isEqualTo(expectedBookDTO);
    }

    @Test
    @DisplayName("Given: id and book, When updateBook, Then: return bookDTO")
    void updateBook() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(MockBook.mockBook(1)));
        when(bookRepository.save(any(Book.class))).thenReturn(MockBook.mockBook(1));

        Optional<BookDTO> expectedBookDTO = Optional.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        Optional<BookDTO> resultBookDTO = bookService.updateBook(1, expectedBookDTO.get());

        verify(bookRepository).save(any(Book.class));
        verify(mapper).mapToDTO(any(Book.class));
        assertThat(resultBookDTO).isEqualTo(expectedBookDTO);
    }

    @Test
    @DisplayName("Given: id, When: delete, Then: success delete")
    void deleteBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(MockBook.mockBook(1)));
        bookService.deleteBook(1);

        verify(bookRepository).deleteById(1);
    }
}
