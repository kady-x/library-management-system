// src/main/java/com/aiu/library/model/Billing.java
package com.aiu.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Billing {

    private Long memberID;
    private double totalFines = 0.0;
    private List<String> paymentHistory = new ArrayList<>();
    public Billing(Long memberID) {
        this.memberID = memberID;
    }

    
    public Long getMemberID() { return memberID; }
    public double getTotalFines() { return totalFines; }
    public List<String> getPaymentHistory() { return new ArrayList<>(paymentHistory); }

    
    public double calculateFine(LocalDate dueDate) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(dueDate)) {
            long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, today);
            double fine = daysLate * 2.0;
            totalFines += fine;
            return fine;
        }
        return 0.0;
    }

    
    public void addPayment(double amount) {
        if (amount > 0) {
            totalFines -= amount;
            if (totalFines < 0) totalFines = 0;
            paymentHistory.add(LocalDate.now() + " → Paid: " + amount + " EGP");
        }
    }

    
    public String getPaymentStatus() {
        if (totalFines <= 0) {
            return "No outstanding fines";
        } else {
            return "Outstanding fine: " + String.format("%.2f", totalFines) + " EGP";
        }
    }
}