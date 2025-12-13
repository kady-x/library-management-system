package com.aiu.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "billings")
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billingID;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member = new Member();

    @Column(name = "total_fines")
    private double totalFines = 0.0;

    @Column(name = "payment_history", length = 2000)
    private String paymentHistoryText = "";

    public Billing() {}

    public Billing(Member member) {
        this.member = member;
    }

    public Integer getMemberID() {
        return this.member.getMemberId();
    }

    public double getTotalFines() {
        return totalFines;
    }

    public void setTotalFines(double totalFines) {
        this.totalFines = totalFines;
    }

    public String getPaymentHistoryText() {
        return paymentHistoryText;
    }

    public void setPaymentHistoryText(String paymentHistoryText) {
        this.paymentHistoryText = paymentHistoryText;
    }

    public List<String> getPaymentHistory() {
        if (paymentHistoryText == null || paymentHistoryText.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(List.of(paymentHistoryText.split(";")));
    }

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
            if (totalFines < 0)
                totalFines = 0;
            String paymentEntry = LocalDate.now() + " → Paid: " + amount + " EGP";
            if (paymentHistoryText == null || paymentHistoryText.isEmpty()) {
                paymentHistoryText = paymentEntry;
            } else {
                paymentHistoryText += ";" + paymentEntry;
            }
        }
    }

    public String getPaymentStatus() {
        if (totalFines <= 0) {
            return "No outstanding fines";
        } else {
            return "Outstanding fine: " + String.format("%.2f", totalFines) + " EGP";
        }
    }

    public Integer getId() {
        return billingID;
    }
}
