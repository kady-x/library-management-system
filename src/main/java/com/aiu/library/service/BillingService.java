package com.aiu.library.service;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.Billing;
import com.aiu.library.model.Member;
import com.aiu.library.repository.BillingRepository;
import com.aiu.library.repository.MemberJpaRepository;

@Service
public class BillingService {

    private static final Logger logger = LoggerFactory.getLogger(BillingService.class);
    private final BillingRepository billingRepository;
    private final MemberJpaRepository memberRepository;

    public BillingService(BillingRepository billingRepository, MemberJpaRepository memberRepository) {
        this.billingRepository = billingRepository;
        this.memberRepository = memberRepository;
    }

    public double calculateFine(Integer memberID, LocalDate dueDate) {
        logger.info("Calculating fine for memberId: {}, dueDate: {}", memberID, dueDate);

        if (memberID == null || memberID <= 0) {
            throw new IllegalArgumentException("Valid memberId is required");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("dueDate is required");
        }

        try {
            Billing billing = getOrCreateBilling(memberID);
            double fine = billing.calculateFine(dueDate);
            logger.info("Calculated fine of {} for memberId: {}", fine, memberID);
            return fine;
        } catch (Exception e) {
            logger.error("Error calculating fine for memberId: {}", memberID, e);
            throw new RuntimeException("Failed to calculate fine", e);
        }
    }

    @Transactional
    public void addPayment(Integer memberID, double amount) {
        logger.info("Adding payment of {} for memberId: {}", amount, memberID);

        if (memberID == null || memberID <= 0) {
            throw new IllegalArgumentException("Valid memberId is required");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive");
        }

        try {
            Billing billing = getOrCreateBilling(memberID);
            double previousTotal = billing.getTotalFines();
            billing.addPayment(amount);

            Billing saved = billingRepository.save(billing);
            logger.info("Payment processed successfully. MemberId: {}, Amount: {}, New Total: {}",
                       memberID, amount, saved.getTotalFines());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error processing payment for memberId: {}", memberID, e);
            throw new RuntimeException("Failed to process payment", e);
        }
    }

    public String getPaymentStatus(Integer memberID) {
        logger.info("Getting payment status for memberId: {}", memberID);

        if (memberID == null || memberID <= 0) {
            throw new IllegalArgumentException("Valid memberId is required");
        }

        try {
            Billing billing = getOrCreateBilling(memberID);
            String status = billing.getPaymentStatus();
            logger.info("Payment status for memberId {}: {}", memberID, status);
            return status;
        } catch (Exception e) {
            logger.error("Error getting payment status for memberId: {}", memberID, e);
            throw new RuntimeException("Failed to get payment status", e);
        }
    }

    public Billing getBilling(Integer memberID) {
        logger.info("Getting billing for memberId: {}", memberID);

        if (memberID == null || memberID <= 0) {
            throw new IllegalArgumentException("Valid memberId is required");
        }

        try {
            Billing billing = getOrCreateBilling(memberID);
            logger.info("Retrieved billing for memberId: {}", memberID);
            return billing;
        } catch (Exception e) {
            logger.error("Error getting billing for memberId: {}", memberID, e);
            throw new RuntimeException("Failed to get billing", e);
        }
    }

    private Billing getOrCreateBilling(Integer memberId) {
        Billing billing = billingRepository.findByMemberID(memberId);
        if (billing == null) {
            logger.info("Creating new billing record for memberId: {}", memberId);

            Optional<Member> member = memberRepository.findById(memberId);
            if (!member.isPresent()) {
                throw new IllegalStateException("Member with ID " + memberId + " does not exist");
            }

            billing = new Billing(member.get());
            billing = billingRepository.save(billing);
            logger.info("Created new billing record with ID: {} for memberId: {}",
                       billing.getId(), memberId);
        }
        return billing;
    }
}
