package com.aiu.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PagesController {

    @GetMapping({"/", "/dashboard"})
    public String dashboardPage(Model model) {
        model.addAttribute("currentPage", "dashboard");
        return "pages/dashboard";
    }

    @GetMapping({"/books", "/books/", "/books/list"})
    public String showBooksPage(Model model) {
        model.addAttribute("currentPage", "books");
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

    @GetMapping("/borrow")
    public String showBorrowPage(Model model) {
        model.addAttribute("currentPage", "borrow");
        return "pages/borrow/borrow";
    }

    @GetMapping("/members")
    public String showMembersPage(Model model) {
        model.addAttribute("currentPage", "members");
        return "pages/members/list";
    }

    @GetMapping("/members/add")
    public String showAddMemberPage() {
        return "pages/members/add";
    }

    @GetMapping("/members/edit/{id}")
    public String showEditMemberPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("memberId", id);
        return "pages/members/edit";
    }

    @GetMapping("/members/details/{id}")
    public String showMemberDetailsPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("memberId", id);
        return "pages/members/details";
    }

    @GetMapping("/members/billing/{id}")
    public String showMemberBillingPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("memberId", id);
        return "pages/members/billing";
    }

    @GetMapping("/billing")
    public String showBillingPage(Model model) {
        model.addAttribute("currentPage", "billing");
        return "pages/billing/billing";
    }

    @GetMapping("/reports")
    public String showReportsPage(Model model) {
        model.addAttribute("currentPage", "reports");
        return "pages/reports/index";
    }

    @GetMapping("/waitinglist")
    public String showWaitingListPage(Model model) {
        model.addAttribute("currentPage", "waitinglist");
        return "pages/waitinglist/list";
    }

}
