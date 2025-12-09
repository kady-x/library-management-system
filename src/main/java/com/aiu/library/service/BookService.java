package com.aiu.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.Book;
import com.aiu.library.repository.BookRepository;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Book addBook(Book book) {
        logger.info("Adding new book: {}", book != null ? book.getTitle() : "null");

        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author is required");
        }

        try {
            Book saved = bookRepository.insert(book);
            logger.info("Successfully added book with ID: {}", saved.getBookID());
            return saved;
        } catch (Exception e) {
            logger.error("Failed to add book: {}", book.getTitle(), e);
            throw new RuntimeException("Failed to add book: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Book updateBook(Integer id, Book updated) {
        logger.info("Updating book with ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Book ID is required");
        }
        if (updated == null) {
            throw new IllegalArgumentException("Updated book data cannot be null");
        }
        if (updated.getTitle() == null || updated.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (updated.getAuthor() == null || updated.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author is required");
        }

        try {
            Book existing = bookRepository.findById(id);
            if (existing == null) {
                throw new IllegalStateException("Book with ID " + id + " not found");
            }

            existing.setTitle(updated.getTitle().trim());
            existing.setAuthor(updated.getAuthor().trim());
            existing.setGenre(updated.getGenre() != null ? updated.getGenre().trim() : null);
            existing.setCoverUrl(updated.getCoverUrl() != null ? updated.getCoverUrl().trim() : null);
            existing.setPublicationYear(updated.getPublicationYear());
            existing.setAvailabilityStatus(updated.getAvailabilityStatus());

            Book saved = bookRepository.update(existing);
            logger.info("Successfully updated book with ID: {}", id);
            return saved;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update book with ID: {}", id, e);
            throw new RuntimeException("Failed to update book: " + e.getMessage(), e);
        }
    }

    public List<Book> getAllBooks() {
        logger.debug("Fetching all books");
        try {
            List<Book> books = bookRepository.findAll();
            logger.debug("Retrieved {} books", books.size());
            return books;
        } catch (Exception e) {
            logger.error("Failed to retrieve books", e);
            throw new RuntimeException("Failed to retrieve books", e);
        }
    }

    public List<Book> searchBooks(String query) {
        logger.debug("Searching books with query: {}", query);
        try {
            List<Book> results = bookRepository.searchBooks(query);
            logger.debug("Found {} books matching query: {}", results.size(), query);
            return results;
        } catch (Exception e) {
            logger.error("Failed to search books with query: {}", query, e);
            throw new RuntimeException("Failed to search books", e);
        }
    }

    public Book getBookById(Integer id) {
        logger.debug("Fetching book with ID: {}", id);
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            Book book = bookRepository.findById(id);
            if (book != null) {
                logger.debug("Found book with ID: {}", id);
            } else {
                logger.debug("Book with ID {} not found", id);
            }
            return book;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch book with ID: {}", id, e);
            throw new RuntimeException("Failed to fetch book", e);
        }
    }

    @Transactional
    public void deleteBook(Integer id) {
        logger.info("Deleting book with ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Book ID is required");
        }

        try {
            bookRepository.deleteById(id);
            logger.info("Successfully deleted book with ID: {}", id);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete book with ID: {}", id, e);
            throw new RuntimeException("Failed to delete book: " + e.getMessage(), e);
        }
    }
}
