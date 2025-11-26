package com.aiu.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aiu.library.service.BookService;
import com.aiu.library.model.Book;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping
    public List<Book> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        books.add(new Book(1, "To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, true));
        books.add(new Book(2, "1984", "George Orwell", "Dystopian", 1949, true));
        books.add(new Book(3, "The Great Gatsby", "F. Scott Fitzgerald", "Classic", 1925, false));
        books.add(new Book(4, "The Catcher in the Rye", "J.D. Salinger", "Fiction", 1951, true));
        books.add(new Book(5, "Pride and Prejudice", "Jane Austen", "Romance", 1813, false));
        return books;
    }
}
