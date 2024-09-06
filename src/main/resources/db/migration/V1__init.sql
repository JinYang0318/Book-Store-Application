CREATE TABLE Book
(
    id             INT PRIMARY KEY AUTO_INCREMENT,
    book_title     VARCHAR(50)         NOT NULL,
    book_author    VARCHAR(50) UNIQUE  NOT NULL,
    book_year      VARCHAR(50)         NOT NULL
);

INSERT INTO Book(book_title, book_author, book_year)
VALUES('The Great Gatsby', 'F. Scott Fitzgerald', 1925),
      ('To Kill a Mockingbird', 'Harper Lee', 1960),
      ('1984', 'George Orwell', 1949);