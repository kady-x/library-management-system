// src/main/java/com/aiu/library/repository/BillingRepository.java
package com.aiu.library.repository;

import com.aiu.library.model.Billing;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BillingRepository {

    private final Map<Long, Billing> billingData = new HashMap<>();

    public Billing save(Billing billing) {
        billingData.put(billing.getMemberID(), billing);
        return billing;
    }

    public Billing findByMemberID(Long memberID) {
        return billingData.computeIfAbsent(memberID, Billing::new);
    }

    public void deleteByMemberID(Long memberID) {
        billingData.remove(memberID);
    }
}
