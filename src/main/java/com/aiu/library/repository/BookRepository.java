package com.aiu.library.repository;

import org.springframework.stereotype.Repository;

import com.aiu.library.datastructures.BookBST;
import com.aiu.library.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class BookRepository {

    private final BookBST bst = new BookBST();
    private final BookJpaRepository bookJpaRepository;
    private final AtomicInteger nextId = new AtomicInteger(1);
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

        Integer maxFromJpa = null;
        List<Book> all = bookJpaRepository.findAll();
        if (all != null && !all.isEmpty()) {
            for (Book b : all) {
                if (b != null) bst.addBook(b);
            }

            try {
                maxFromJpa = bookJpaRepository.findMaxBookID();
            } catch (Exception ex) {
                maxFromJpa = all.stream()
                        .map(Book::getBookID)
                        .filter(id -> id != null)
                        .max(Integer::compareTo)
                        .orElse(null);
            }
        }
        Integer maxFromBst = bst.findMaxId(bst.getRoot());
        int max = 0;
        if (maxFromJpa != null) max = Math.max(max, maxFromJpa);
        if (maxFromBst != null) max = Math.max(max, maxFromBst);
        this.nextId.set(max + 1);

        initialized = true;
    }

    public void insert(Book book) {
        if (book.getBookID() == null) {
            book.setBookID(generateNextId());
        }
        Book saved = bookJpaRepository.save(book);
        bst.addBook(saved);
    }

    public void update(Book book) {
        Book saved = bookJpaRepository.save(book);
        bst.updateBookInfo(saved);
    }

    public void deleteById(Integer id) {
        bookJpaRepository.deleteById(id);
        bst.delete(id);
    }

    public synchronized Integer generateNextId() {
        return Integer.valueOf(nextId.getAndIncrement());
    }

    public Book findById(Integer id) {
        return bst.searchByID(id);
    }
    
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        bst.inOrderTraversal(bst.getRoot(), books);
        return books;
    }

    public List<Book> searchBooks(String query) {
        return bst.searchBooks(query);
    }
}
