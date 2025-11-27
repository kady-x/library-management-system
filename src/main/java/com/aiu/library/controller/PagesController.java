package com.aiu.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PagesController {

    @GetMapping({"/", "/dashboard"})
    public String dashboardPage() {
        return "pages/dashboard";
    }

    @GetMapping({"/books", "/books/", "/books/list"})
    public String showBooksPage() {
        return "pages/books/list";
    }

    @GetMapping("/books/add")
    public String showAddBookPage() {
        return "pages/books/add";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditBookPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bookId", id);
        return "pages/books/edit";
    }

}

