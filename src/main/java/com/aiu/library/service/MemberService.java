package com.aiu.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.Member;
import com.aiu.library.model.MemberWithBillingRequest;
import com.aiu.library.repository.MemberRepository;


@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;
    private final BillingService billingService;

    public MemberService(MemberRepository memberRepository, BillingService billingService) {
        this.memberRepository = memberRepository;
        this.billingService = billingService;
    }

    @Transactional
    public Member registerMember(Member member) {
        logger.info("Registering new member: {}", member != null ? member.getName() : "null");

        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null");
        }
        if (member.getName() == null || member.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required");
        }
        if (member.getContactInfo() == null || member.getContactInfo().trim().isEmpty()) {
            throw new IllegalArgumentException("Member contact information is required");
        }

        try {
            memberRepository.insert(member);
            Member saved = memberRepository.findById(member.getMemberId());
            logger.info("Successfully registered member: {}", member.getName());
            return saved;
        } catch (Exception e) {
            logger.error("Failed to register member: {}", member.getName(), e);
            throw new RuntimeException("Failed to register member: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Member registerMemberWithBilling(MemberWithBillingRequest request) {
        logger.info("Registering new member with billing: {}", request.getName());

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setContactInfo(request.getContactInfo());
        member.setMembershipDate(request.getMembershipDate());

        Member savedMember = registerMember(member);

        if (request.hasBillingInfo()) {
            try {
                initBilling(savedMember.getMemberId(), request.getTotalFines(), request.getPaymentHistoryText());
                logger.info("Successfully initialized billing for member: {}", savedMember.getName());
            } catch (Exception e) {
                logger.error("Failed to initialize billing for member: {}", savedMember.getName(), e);

            }
        }

        return savedMember;
    }

    @Transactional
    public Member updateMember(Integer id, Member updated) {
        logger.info("Updating member with ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Member ID is required");
        }
        if (updated == null) {
            throw new IllegalArgumentException("Updated member data cannot be null");
        }
        if (updated.getName() == null || updated.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Member name is required");
        }
        if (updated.getContactInfo() == null || updated.getContactInfo().trim().isEmpty()) {
            throw new IllegalArgumentException("Member contact information is required");
        }

        try {
            Member existing = memberRepository.findById(id);
            if (existing == null) {
                throw new IllegalStateException("Member with ID " + id + " not found");
            }

            updated.setMemberId(id);
            updated.setName(updated.getName().trim());
            updated.setContactInfo(updated.getContactInfo().trim());
            updated.setMembershipDate(updated.getMembershipDate() != null ? 
                updated.getMembershipDate().trim() : null);

            memberRepository.update(updated);
            Member saved = memberRepository.findById(id);
            logger.info("Successfully updated member with ID: {}", id);
            return saved;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update member with ID: {}", id, e);
            throw new RuntimeException("Failed to update member: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Member updateMemberWithBilling(Integer id, MemberWithBillingRequest request) {
        logger.info("Updating member with billing, ID: {}", id);

        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setContactInfo(request.getContactInfo());
        member.setMembershipDate(request.getMembershipDate());

        Member updatedMember = updateMember(id, member);

        if (request.hasBillingInfo()) {
            try {
                initBilling(id, request.getTotalFines(), request.getPaymentHistoryText());
                logger.info("Successfully updated billing for member ID: {}", id);
            } catch (Exception e) {
                logger.error("Failed to update billing for member ID: {}", id, e);
            }
        }

        return updatedMember;
    }

    @Transactional
    public void initBilling(Integer memberId, Double totalFines, String paymentHistoryText) {
        logger.info("Initializing billing for memberId: {}, totalFines: {}", memberId, totalFines);

        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("Valid memberId is required");
        }

        try {
            billingService.setBillingInfo(memberId, totalFines, paymentHistoryText);
            logger.info("Successfully initialized/updated billing for memberId: {}", memberId);
        } catch (Exception e) {
            logger.error("Failed to initialize billing for memberId: {}", memberId, e);
            throw new RuntimeException("Failed to initialize billing: " + e.getMessage(), e);
        }
    }

    public List<Member> searchMembers(String query) {
        logger.debug("Searching members with query: {}", query);
        try {
            if (query == null || query.trim().isEmpty()) {
                return getAllMembers();
            }
            List<Member> allMembers = getAllMembers();
            List<Member> results = allMembers.stream()
                .filter(member -> 
                    (member.getName() != null && member.getName().toLowerCase().contains(query.toLowerCase())) ||
                    (member.getContactInfo() != null && member.getContactInfo().toLowerCase().contains(query.toLowerCase()))
                )
                .toList();
            logger.debug("Found {} members matching query: {}", results.size(), query);
            return results;
        } catch (Exception e) {
            logger.error("Failed to search members with query: {}", query, e);
            throw new RuntimeException("Failed to search members", e);
        }
    }

    public List<Member> getAllMembers() {
        logger.debug("Fetching all members");
        try {
            List<Member> members = memberRepository.findAll();
            logger.debug("Retrieved {} members", members.size());
            return members;
        } catch (Exception e) {
            logger.error("Failed to retrieve members", e);
            throw new RuntimeException("Failed to retrieve members", e);
        }
    }

    public Member getMemberById(Integer id) {
        logger.debug("Fetching member with ID: {}", id);
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            Member member = memberRepository.findById(id);
            if (member != null) {
                logger.debug("Found member with ID: {}", id);
            } else {
                logger.debug("Member with ID {} not found", id);
            }
            return member;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to fetch member with ID: {}", id, e);
            throw new RuntimeException("Failed to fetch member with ID: " + id, e);
        }
    }

    @Transactional
    public void deleteMember(Integer id) {
        logger.info("Deleting member with ID: {}", id);

        if (id == null) {
            throw new IllegalArgumentException("Member ID is required");
        }

        try {
            memberRepository.deleteById(id);
            logger.info("Successfully deleted member with ID: {}", id);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to delete member with ID: {}", id, e);
            throw new RuntimeException("Failed to delete member: " + e.getMessage(), e);
        }
    }
}
