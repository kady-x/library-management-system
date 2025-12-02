
package com.aiu.library.controller;

import com.aiu.library.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping()
public class BillingController {

    @Autowired
    private BillingService billingService;

    
    @GetMapping()
    public double calculateFine(
            @PathVariable Long memberID,
            @RequestParam String dueDate) {  
        LocalDate date = LocalDate.parse(dueDate);
        return billingService.calculateFine(memberID, date);
    }

    
    @PostMapping()
    public String payFine(
            @PathVariable Long memberID,
            @RequestParam double amount) {
        billingService.addPayment(memberID, amount);
        return billingService.getPaymentStatus(memberID);
    }

    
    @GetMapping()
    public String getStatus(@PathVariable Long memberID) {
        return billingService.getPaymentStatus(memberID);
    }
}