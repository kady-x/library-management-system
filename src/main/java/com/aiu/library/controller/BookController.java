package com.aiu.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable int id, @RequestBody Book book) {
        bookService.updateBook(id, book);
    }
}