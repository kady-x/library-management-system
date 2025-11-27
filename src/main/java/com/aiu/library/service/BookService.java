package com.aiu.library.service;

import com.aiu.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import com.aiu.library.model.Book;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        if (book.getBookID() == null) {
            book.setBookID(BookRepository.generateNextId());
        }else if (bookRepository.findById(book.getBookID()) != null) {
            throw new RuntimeException("Book ID already exists.");
        }
        bookRepository.insert(book);
    }

    public void updateBook(Integer id, Book updated) {
        Book existing = bookRepository.findById(id);
        if (existing == null) throw new RuntimeException("Book not found.");

        existing.setTitle(updated.getTitle());
        existing.setAuthor(updated.getAuthor());
        existing.setGenre(updated.getGenre());
        existing.setPublicationYear(updated.getPublicationYear());
        existing.setAvailabilityStatus(updated.getAvailabilityStatus());
    }

    public Book getBookDetails(Integer id) {
        return bookRepository.findById(id);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Integer id) {
        return bookRepository.findById(id);
    }

}
