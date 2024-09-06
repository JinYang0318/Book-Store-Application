package com.example.Book.Store.Application.controller;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.exception.BookNotFoundException;
import com.example.Book.Store.Application.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(value = "/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Integer id) {
        BookDTO bookDTO = bookService.getBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
        return ResponseEntity.ok(bookDTO);
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getBookByIds(
            @RequestParam(value = "id", required = false) List<Integer> ids) {
        List<BookDTO> bookDTOList = bookService.getBookByIds(ids);
        List<Integer> returnedIds = bookDTOList
                .stream()
                .map(BookDTO::id)
                .toList();

        List<Integer> missingIds = ids.stream()
                .filter(id -> !returnedIds.contains(id))
                .toList();

        HttpHeaders headers = new HttpHeaders();
        if (!missingIds.isEmpty()) {
            headers.add("X-MISSING-SET", missingIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        log.info("X-MISSING-SET {}", missingIds);
        return ResponseEntity.ok().headers(headers).body(bookDTOList);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOList = bookService.getAllBooks();
        return ResponseEntity.ok().body(bookDTOList);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO)
                .map(passenger -> ResponseEntity.status(HttpStatus.CREATED).body(passenger))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable("id") Integer id,
            @Valid @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO)
                .map(book -> ResponseEntity.status(HttpStatus.OK).body(book))
                .orElseThrow(() -> new BookNotFoundException("Book Id With " + id + " not found"));
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteBook(@PathVariable("id") Integer id) {
        BookDTO bookDTO = bookService.getBookById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Id With " + id + " not found"));
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.OK).body("Book with ID " + id + " successfully deleted.");
    }
}
