package com.aiu.library.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aiu.library.model.Billing;

@Repository
public class BillingRepository {

    private final Map<Integer, Billing> billingData = new HashMap<>();

    public Billing save(Billing billing) {
        billingData.put(billing.getMemberID(), billing);
        return billing;
    }

    public Billing findByMemberID(Integer memberID) {
        return billingData.get(memberID);
    }

    public void deleteByMemberID(Integer memberID) {
        billingData.remove(memberID);
    }
}
