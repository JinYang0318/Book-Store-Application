package com.example.Book.Store.Application.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOOK")
@Data
public class Book {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "book_author")
    private String bookAuthor;

    @Column(name = "book_year")
    private Integer bookYear;
}
