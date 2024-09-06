package com.example.Book.Store.Application.controllerTest;

import com.example.Book.Store.Application.controller.BookController;
import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.mock.MockBook;
import com.example.Book.Store.Application.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {
    private final static String BOOK_URL = "/api/book";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private static Stream<Arguments> invalidParam() {
        return Stream.of(
                Arguments.of("blank request param", "?id= "),
                Arguments.of("empty request param", "?id=")
        );
    }

    private static Stream<Arguments> emptyCreateValue() {
        return Stream.of(
                Arguments.of("empty bookTitle but have bookAuthor and bookYear", MockBook.mockBookDTO(1, null, "Author", 2024), "{\"bookTitle\":\"BookTitle is required\"}"),
                Arguments.of("empty bookAuthor but have bookTitle and bookYear", MockBook.mockBookDTO(1, "Title", null, 2024), "{\"bookAuthor\":\"Book Author is required\"}"),
                Arguments.of("empty bookYear but have bookAuthor and bookTitle", MockBook.mockBookDTO(1, "Title", "Author", null), "{\"bookYear\":\"Book Year is required\"}"),
                Arguments.of("both value are empty", MockBook.mockBookDTO(1, null, null, null), "{\"bookYear\":\"Book Year is required\",\"bookAuthor\":\"Book Author is required\",\"bookTitle\":\"BookTitle is required\"}")
        );
    }

    private static Stream<Arguments> emptyUpdateValue() {
        return Stream.of(
                Arguments.of("empty bookTitle but have bookAuthor and bookYear", MockBook.mockBookDTO(1, null, "Author", 2024), "{\"bookTitle\":\"BookTitle is required\"}"),
                Arguments.of("empty bookAuthor but have bookTitle and bookYear", MockBook.mockBookDTO(1, "Title", null, 2024), "{\"bookAuthor\":\"Book Author is required\"}"),
                Arguments.of("empty bookYear but have bookAuthor and bookTitle", MockBook.mockBookDTO(1, "Title", "Author", null), "{\"bookYear\":\"Book Year is required\"}"),
                Arguments.of("both value are empty", MockBook.mockBookDTO(1, null, null, null), "{\"bookYear\":\"Book Year is required\",\"bookAuthor\":\"Book Author is required\",\"bookTitle\":\"BookTitle is required\"}")
        );
    }

    @Test
    @DisplayName("Given: valid id, When: GET /api/book/1, Then: return bookDTO")
    void getBookById() throws Exception {
        BookDTO bookDTO = MockBook.mockBookDTO(1, "Title", "Author", 2024);
        when(bookService.getBookById(anyInt())).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(get(BOOK_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Optional.of(bookDTO))));

        verify(bookService).getBookById(1);
    }

    @Test
    @DisplayName("Given: id not found, When: GET /api/book/999, Then: return 404 not found")
    void getBookByIdNotFound() throws Exception {
        when(bookService.getBookById(anyInt())).thenReturn(Optional.empty());
        mockMvc.perform(get(BOOK_URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID 999 not found"));
        verify(bookService).getBookById(999);
    }

    @Test
    @DisplayName("Given: invalid id , When: GET /api/book/a, Then: return 400 status bad request")
    void getBookByIdInvalidParam() throws Exception {
        mockMvc.perform(get(BOOK_URL + "/a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Failed to convert 'id' with value: 'a'"));

        verifyNoInteractions(bookService);
    }

    @Test
    @DisplayName("Given: valid ids, When: GET /api/book?id=1, Then: return bookDTO list")
    void getBookByIds() throws Exception {
        List<BookDTO> bookDTOList = List.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookService.getBookByIds(anyList())).thenReturn(bookDTOList);

        mockMvc.perform(get(BOOK_URL + "?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDTOList)));

        verify(bookService).getBookByIds(List.of(1));
    }

    @Test
    @DisplayName("Given: ids not found, When: GET /api/book?id=999, Then: return 200 with []")
    void getBookByIdsNotFound() throws Exception {
        when(bookService.getBookByIds(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BOOK_URL + "?id=999"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookService).getBookByIds(List.of(999));
    }

    @Test
    @DisplayName("Given: ids one found and one not found, When: GET /api/book?id=1&id=999, Then: return 200 with header X-MISSING-SET")
    void getBookByIdsWithOneDataNotFound() throws Exception {
        List<BookDTO> bookDTOList = List.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookService.getBookByIds(anyList()))
                .thenReturn(bookDTOList);

        mockMvc.perform(get(BOOK_URL + "?id=1&id=999"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDTOList)))
                .andExpect(header().string("X-MISSING-SET", "999"));

        verify(bookService).getBookByIds(List.of(1, 999));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidParam")
    void getBookByIdsInvalidParam(String scenario, String requestParam) throws Exception {
        when(bookService.getBookByIds(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BOOK_URL + requestParam))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(bookService).getBookByIds(anyList());
    }

    @Test
    @DisplayName("Given : - , When: GET /api/book/all, Then: return 200 With list bookDTO")
    void getAllBooks() throws Exception {
        List<BookDTO> bookDTOList = List.of(MockBook.mockBookDTO(1, "Title", "Author", 2024));
        when(bookService.getAllBooks())
                .thenReturn(bookDTOList);

        mockMvc.perform(get(BOOK_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDTOList)));

        verify(bookService).getAllBooks();
    }

    @Test
    @DisplayName("Given: book, When: POST /api/book, Then: return 201 created")
    void createBook() throws Exception {
        BookDTO bookDTO = MockBook.mockBookDTO(1, "Title", "Author", 2024);
        when(bookService.createBook(any(BookDTO.class))).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(post(BOOK_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDTO)));

        verify(bookService).createBook(any(BookDTO.class));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("emptyCreateValue")
    void createBookWithEmpty(String scenario, BookDTO bookDTO, String expectedMessage) throws Exception {
        when(bookService.createBook(any(BookDTO.class))).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(post(BOOK_URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedMessage));

        verify(bookService, never()).createBook(any(BookDTO.class));
    }

    @Test
    @DisplayName("Given: valid id, When: PUT /api/book/1, Then: return 200 with update bookDTO")
    void updateBook() throws Exception {
        BookDTO bookDTO = MockBook.mockBookDTO(1, "Title", "Author", 2024);
        when(bookService.updateBook(anyInt(), any(BookDTO.class))).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(put((BOOK_URL + "/1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDTO)));

        verify(bookService).updateBook(1, bookDTO);
    }

    @Test
    @DisplayName("Given: id not found, When: PUT /api/book/999, Then: return 404 with not found")
    void updateBookWithNotFound() throws Exception {
        BookDTO bookDTO =  MockBook.mockBookDTO(1, "Title", "Author", 2024);
        mockMvc.perform(put((BOOK_URL + "/999"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book Id With " + 999 + " not found"));

        verify(bookService).updateBook(999, bookDTO);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("emptyUpdateValue")
    void updateBookWithEmpty(String scenario, BookDTO bookDTO, String expectedMessage) throws Exception {
        when(bookService.updateBook(anyInt(), any(BookDTO.class))).thenReturn(Optional.of(bookDTO));

        mockMvc.perform(put((BOOK_URL + "/1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedMessage));

        verify(bookService, never()).updateBook(1, bookDTO);
    }

    @Test
    @DisplayName("Given: valid id , When: DELETE /api/book?id=1, Then: return 200 with successfully deleted")
    void deleteBook() throws Exception {
        BookDTO bookDTO = MockBook.mockBookDTO(1, "Title", "Author", 2024);
        when(bookService.getBookById(1))
                .thenReturn(Optional.of(bookDTO));

        bookService.deleteBook(1);
        mockMvc.perform(delete(BOOK_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book with ID 1 successfully deleted."));

        verify(bookService).getBookById(1);
        verify(bookService, times(2)).deleteBook(1);
    }

    @Test
    @DisplayName("Given: id not found, When: DELETE /api/book?id=999, Then: return 404 with not found")
    void deleteBookWithIdNotFound() throws Exception {
        bookService.deleteBook(999);
        mockMvc.perform(delete(BOOK_URL + "/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book Id With " + 999 + " not found"));

        verify(bookService).deleteBook(999);
    }

    @Test
    @DisplayName("Given: Invalid id , When: DELETE /api/book?id=1a, Then: return 400 with Bad Request")
    void deleteBookWithInvalidId() throws Exception {
        mockMvc.perform(delete(BOOK_URL + "/1a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Failed to convert 'id' with value: '1a'"));

        verifyNoInteractions(bookService);
    }
}
