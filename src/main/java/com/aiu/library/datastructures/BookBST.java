package com.aiu.library.datastructures;

import com.aiu.library.model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookBST {
    private BookNode root;

    public BookBST() {
        this.root = null;
    }
    
    public BookNode getRoot() {
        return root;
    }

    public void addBook(Book book) {
        if (root == null) {
            root = new BookNode(book);
        } else {
            root.insert(book);
        }
    }

    public void updateBookInfo(Book updatedBook) {
        delete(updatedBook.getBookID());
        addBook(updatedBook);
    }

    public void delete(Integer id) {
        root = deleteRec(root, id);
    }

    private BookNode deleteRec(BookNode node, Integer id) {
        if (node == null) return null;

        if (id < node.data.getBookID()) {
            node.left = deleteRec(node.left, id);
        } else if (id > node.data.getBookID()) {
            node.right = deleteRec(node.right, id);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            BookNode min = findMin(node.right);
            node.data = min.data;
            node.right = deleteRec(node.right, min.data.getBookID());
        }

        return node;
    }

    public Book searchByID(Integer id) {
        if (root == null) return null;
        return root.searchById(id);
    }

    public Book searchByTitle(String title) {
        if (root == null) return null;
        return root.searchByTitle(title);
    }

    private BookNode findMin(BookNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        if (root != null)
            root.getAllBooks(books);
        return books;
    }

    public void inOrderTraversal(BookNode node, List<Book> books) {
        if (node != null) {
            inOrderTraversal(node.left, books);
            books.add(node.data);
            inOrderTraversal(node.right, books);
        }
    }

    public Integer findMaxId(BookNode node) {
        if (node == null) return null;
        while (node.right != null) {
            node = node.right;
        }
        return node.data.getBookID();
    }

}
