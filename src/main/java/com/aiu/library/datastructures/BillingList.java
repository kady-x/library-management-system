package com.aiu.library.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.aiu.library.model.Billing;

public class BillingList {

    private List<Billing> billings;

    public BillingList() {
        this.billings = new ArrayList<>();
    }

    public void addBilling(Billing billing) {
        if (billing != null) {
            this.billings.add(billing);
        }
    }

    public boolean removeBilling(int id) {
        return this.billings.removeIf(billing -> billing.getId().equals(id));
    }

    public Optional<Billing> getBillingById(Long id) {
        return this.billings.stream()
                .filter(billing -> billing.getId().equals(id))
                .findFirst();
    }

    public Optional<Billing> getBillingByMemberId(Long memberId) {
        return this.billings.stream()
                .filter(billing -> billing.getMemberID().equals(memberId))
                .findFirst();
    }

    public List<Billing> getAllBillings() {
        return new ArrayList<>(this.billings);
    }

    public Double getTotalFines() {
        return this.billings.stream()
                .mapToDouble(Billing::getTotalFines)
                .sum();
    }

    public int size() {
        return this.billings.size();
    }

    public boolean isEmpty() {
        return this.billings.isEmpty();
    }

    public void clear() {
        this.billings.clear();
    }

}
