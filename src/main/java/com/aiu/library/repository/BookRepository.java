package com.aiu.library.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.datastructures.BookBST;
import com.aiu.library.model.Book;

@Repository
public class BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(BookRepository.class);
    private final BookBST bst = new BookBST();
    private final BookJpaRepository bookJpaRepository;
    private volatile boolean initialized = false;

    public BookRepository(BookJpaRepository bookJpaRepository) {
        this.bookJpaRepository = bookJpaRepository;
        initializeFromDatabaseIfNeeded();
    }

    public void ensureInitialized() {
        initializeFromDatabaseIfNeeded();
    }

    private synchronized void initializeFromDatabaseIfNeeded() {
        if (initialized) return;

        try {
            List<Book> all = bookJpaRepository.findAll();
            if (all != null && !all.isEmpty()) {
                for (Book b : all) {
                    if (b != null) bst.addBook(b);
                }
            }
            initialized = true;
            logger.info("BookRepository initialized with {} books", all != null ? all.size() : 0);
        } catch (Exception e) {
            logger.error("Failed to initialize BookRepository from database", e);
            throw new RuntimeException("Failed to initialize book repository", e);
        }
    }

    @Transactional
    public Book insert(Book book) {
        logger.info("Inserting new book: {}", book.getTitle());

        try {
            if (book == null) {
                throw new IllegalArgumentException("Book cannot be null");
            }
            if (book.getBookID() != null) {
                if (findById(book.getBookID()) != null) {
                    throw new IllegalStateException("Book with ID " + book.getBookID() + " already exists");
                }
            }

            Book saved = bookJpaRepository.save(book);

            bst.addBook(saved);

            logger.info("Successfully inserted book with ID: {}", saved.getBookID());
            return saved;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to insert book: {}", book.getTitle(), e);
            throw new RuntimeException("Failed to insert book", e);
        }
    }

    @Transactional
    public Book update(Book book) {
        logger.info("Updating book with ID: {}", book.getBookID());

        try {
            if (book == null) {
                throw new IllegalArgumentException("Book cannot be null");
            }
            if (book.getBookID() == null) {
                throw new IllegalArgumentException("Book ID cannot be null for update");
            }

            Book existing = bst.searchByID(book.getBookID());
            if (existing == null) {
                throw new IllegalStateException("Book with ID " + book.getBookID() + " not found");
            }

            Book saved = bookJpaRepository.save(book);

            bst.updateBookInfo(saved);

            logger.info("Successfully updated book with ID: {}", saved.getBookID());
            return saved;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update book with ID: {}", book.getBookID(), e);
            throw new RuntimeException("Failed to update book", e);
        }
    }

    @Transactional
    public void deleteById(Integer id) {
        logger.info("Deleting book with ID: {}", id);

        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }

            Book existing = bst.searchByID(id);
            if (existing == null) {
                throw new IllegalStateException("Book with ID " + id + " not found");
            }

            bookJpaRepository.deleteById(id);

            bst.delete(id);

            logger.info("Successfully deleted book with ID: {}", id);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete book with ID: {}", id, e);
            throw new RuntimeException("Failed to delete book", e);
        }
    }

    public Book findById(Integer id) {
        if (id == null) return null;
        ensureInitialized();
        return bst.searchByID(id);
    }

    public List<Book> findAll() {
        ensureInitialized();
        List<Book> books = new ArrayList<>();
        bst.inOrderTraversal(bst.getRoot(), books);
        return books;
    }

    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>();
        }
        ensureInitialized();
        return bst.searchBooks(query.trim().toLowerCase());
    }
}
