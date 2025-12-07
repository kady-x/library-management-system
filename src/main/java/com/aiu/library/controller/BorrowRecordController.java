package com.aiu.library.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.aiu.library.service.BorrowService;
import com.aiu.library.model.BorrowRecord;
import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowRecordController {
    @Autowired
    private final BorrowService service;

    public BorrowRecordController(BorrowService service) {
        this.service = service;
    }

    @GetMapping
    public List<BorrowRecord> getAll() {
        return service.getAllRecords();
    }

    static class IssueRequest {
        public Integer memberId;
        public Integer bookId;
        public Integer loanDays;
    }

    @PostMapping("/issue")
    public BorrowRecord issue(@RequestBody IssueRequest req) {
        int days = req.loanDays == null ? 14 : req.loanDays;
        return service.issueBook(req.memberId, req.bookId, days);
    }

    @PostMapping("/return/{id}")
    public BorrowRecord returnBook(@PathVariable Integer id) {
        return service.returnBook(id);
    }

    @PostMapping("/renew/{id}")
    public BorrowRecord renew(@PathVariable Integer id, @RequestParam(required = false) Integer days) {
        int extra = days == null ? 7 : days;
        return service.renewBook(id, extra);
    }
}
