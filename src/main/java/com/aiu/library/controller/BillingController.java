package com.aiu.library.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiu.library.model.Billing;
import com.aiu.library.service.BillingService;

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
    public Map<String, Object> getStatus(@PathVariable Integer memberID) {
        Billing billing = service.getBilling(memberID);
        return Map.of(
            "totalFines", billing.getTotalFines(),
            "paymentHistory", billing.getPaymentHistory(),
            "status", billing.getPaymentStatus()
        );
    }
}
