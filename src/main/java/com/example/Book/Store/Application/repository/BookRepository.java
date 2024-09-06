package com.example.Book.Store.Application.repository;

import com.example.Book.Store.Application.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
   Optional<Book> findById(Integer id);
}
