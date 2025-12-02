// src/main/java/com/aiu/library/service/BillingService.java
package com.aiu.library.service;

import com.aiu.library.model.Billing;
import com.aiu.library.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    public double calculateFine(Long memberID, LocalDate dueDate) {
        Billing billing = billingRepository.findByMemberID(memberID);
        return billing.calculateFine(dueDate);
    }

    public void addPayment(Long memberID, double amount) {
        Billing billing = billingRepository.findByMemberID(memberID);
        billing.addPayment(amount);
        billingRepository.save(billing);
    }

    public String getPaymentStatus(Long memberID) {
        Billing billing = billingRepository.findByMemberID(memberID);
        return billing.getPaymentStatus();
    }

    public Billing getBilling(Long memberID) {
        return billingRepository.findByMemberID(memberID);
    }
}
