package com.aiu.library.repository;

import org.springframework.stereotype.Repository;
import com.aiu.library.datastructures.BookBST;
import com.aiu.library.model.Book;

import java.util.ArrayList;
import java.util.List;


@Repository
public class BookRepository {

    private static BookBST bst = new BookBST();

    public void insert(Book book) {
        bst.addBook(book);
    }

    public void update(Book book) {
        bst.updateBookInfo(book);
    }

    public void delete(Integer id) {
        bst.delete(id);
    }

    public static Integer generateNextId() {
        Integer maxId = bst.findMaxId(bst.getRoot());
        return (maxId == null) ? 1 : maxId + 1;
    }


    public Book findById(Integer id) {
        return bst.searchByID(id);
    }
    
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        bst.inOrderTraversal(bst.getRoot(), books);
        return books;
    }
}
