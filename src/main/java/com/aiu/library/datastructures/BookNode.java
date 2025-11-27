package com.aiu.library.datastructures;

import com.aiu.library.model.Book;
import java.util.List;

public class BookNode {
    public Book data;
    public BookNode left;
    public BookNode right;

    public BookNode(Book data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void insert(Book book) {
        if (book.getBookID() < this.data.getBookID()) {
            if (this.left == null)
                this.left = new BookNode(book);
            else
                this.left.insert(book);
        } else {
            if (this.right == null)
                this.right = new BookNode(book);
            else
                this.right.insert(book);
        }
    }

    public Book searchById(Integer id) {
        if (id.equals(this.data.getBookID())) return this.data;

        if (id < this.data.getBookID()) {
            return (left == null) ? null : left.searchById(id);
        } else {
            return (right == null) ? null : right.searchById(id);
        }
    }

    public Book searchByTitle(String title) {
        if (this.data.getTitle().equalsIgnoreCase(title)) return this.data;

        Book result = null;
        
        if (this.left != null) result = left.searchByTitle(title);
        if (result != null) return result;

        if (this.right != null) result = right.searchByTitle(title);
        return result;
    }
    
    public void getAllBooks(List<Book> list) {
        if (this.left != null)
            this.left.getAllBooks(list);

        list.add(this.data);

        if (this.right != null)
            this.right.getAllBooks(list);
    }
}
