package com.aiu.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PagesController {

    @GetMapping({"/", "/dashboard"})
    public String dashboardPage() {
        return "pages/dashboard";
    }

    @GetMapping("/books")
    public String showBooksPage() {
        return "pages/books/list";
    }

    @GetMapping("/books/add")
    public String showAddBookPage() {
        return "pages/books/add";
    }
}

