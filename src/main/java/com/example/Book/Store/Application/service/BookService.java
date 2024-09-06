package com.example.Book.Store.Application.service;

import com.example.Book.Store.Application.dto.BookDTO;
import com.example.Book.Store.Application.mapper.Mapper;
import com.example.Book.Store.Application.model.Book;
import com.example.Book.Store.Application.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final Mapper<Book, BookDTO> mapper;
    private final BookRepository bookRepository;

    public Optional<BookDTO> getBookById(Integer id){
        return bookRepository.findById(id)
                .map(mapper::mapToDTO);
    }

    public List<BookDTO> getBookByIds(List<Integer> ids) {
        return bookRepository.findAllById(ids)
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(mapper::mapToDTO)
                .toList();
    }

    public Optional<BookDTO> createBook(BookDTO bookDTO) {
        return Optional.of(bookRepository.save(mapper.mapToEntity(bookDTO)))
                .map(mapper::mapToDTO);
    }

    public Optional<BookDTO> updateBook(Integer id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setBookAuthor(bookDTO.bookAuthor());
                    existingBook.setBookTitle(bookDTO.bookTitle());
                    existingBook.setBookYear(bookDTO.bookYear());

                    Book updatedBook = bookRepository.save(existingBook);

                    return mapper.mapToDTO(updatedBook);
                });
    }

    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }
}
