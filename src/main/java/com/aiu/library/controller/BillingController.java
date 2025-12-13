package com.aiu.library.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @GetMapping("/calculate/{memberID}")
    public double calculateFine(
            @PathVariable Integer memberID,
            @RequestParam String dueDate) {
        LocalDate date = LocalDate.parse(dueDate);
        return service.calculateFine(memberID, date);
    }

    @PostMapping("/{memberID}/payment")
    public String processPayment(
            @PathVariable Integer memberID,
            @RequestBody Map<String, Object> paymentData) {
        
        Double amount = null;
        if (paymentData.containsKey("amount")) {
            Object amountValue = paymentData.get("amount");
            if (amountValue != null) {
                amount = ((Number) amountValue).doubleValue();
            }
        }
        
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Valid payment amount is required");
        }
        
        service.addPayment(memberID, amount);
        return service.getPaymentStatus(memberID);
    }

    @GetMapping("/status/{memberID}")
    public Map<String, Object> getStatus(@PathVariable Integer memberID) {
        Billing billing = service.getBilling(memberID);
        return Map.of(
            "totalFines", billing.getTotalFines(),
            "paymentHistoryText", billing.getPaymentHistoryText(),
            "paymentHistory", billing.getPaymentHistory(),
            "status", billing.getPaymentStatus()
        );
    }

    @PostMapping("/{memberID}/info")
    public String setBillingInfo(
            @PathVariable Integer memberID,
            @RequestBody Map<String, Object> billingData) {
        
        Double totalFines = null;
        String paymentHistoryText = null;
        
        if (billingData.containsKey("totalFines")) {
            Object finesValue = billingData.get("totalFines");
            if (finesValue != null) {
                totalFines = ((Number) finesValue).doubleValue();
            }
        }
        
        if (billingData.containsKey("paymentHistoryText")) {
            paymentHistoryText = (String) billingData.get("paymentHistoryText");
        }
        
        service.setBillingInfo(memberID, totalFines, paymentHistoryText);
        return "Billing information saved successfully";
    }
}
