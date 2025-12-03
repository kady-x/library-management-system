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

    @GetMapping()
    public double calculateFine(
            @PathVariable Long memberID,
            @RequestParam String dueDate) {  
        LocalDate date = LocalDate.parse(dueDate);
        return service.calculateFine(memberID, date);
    }

    @PostMapping()
    public String payFine(
            @PathVariable Long memberID,
            @RequestParam double amount) {
        service.addPayment(memberID, amount);
        return service.getPaymentStatus(memberID);
    }

    @GetMapping("/status/{memberID}")
    public String getStatus(@PathVariable Long memberID) {
        return service.getPaymentStatus(memberID);
    }
}