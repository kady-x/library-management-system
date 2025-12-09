package com.aiu.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aiu.library.service.BillingService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private final BillingService service;
    
    public BillingController(BillingService service) {
        this.service = service;
    }

    @GetMapping("/{memberID}")
    public double calculateFine(
            @PathVariable Integer memberID,
            @RequestParam String dueDate) {
        LocalDate date = LocalDate.parse(dueDate);
        return service.calculateFine(memberID, date);
    }

    @PostMapping("/{memberID}")
    public String payFine(
            @PathVariable Integer memberID,
            @RequestParam double amount) {
        service.addPayment(memberID, amount);
        return service.getPaymentStatus(memberID);
    }

    @GetMapping("/status/{memberID}")
    public String getStatus(@PathVariable Integer memberID) {
        return service.getPaymentStatus(memberID);
    }
}