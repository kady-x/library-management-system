package com.aiu.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aiu.library.model.Member;
import com.aiu.library.repository.MemberRepository;

/**
 * Service class for managing library members operations.
 * Provides methods for registering, updating, retrieving, and deleting members
 * with comprehensive logging, input validation, and error handling.
 */
@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final MemberRepository memberRepository;

    /**
     * Constructs a MemberService with the required MemberRepository dependency.
     *
     * @param memberRepository the repository for member data access operations
     */
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Registers a new member in the library system.
     * Validates the member data and handles any exceptions that may occur during registration.
     *
     * @param member the member object to be registered
     * @return the registered member with generated ID
     * @throws IllegalArgumentException if member data is invalid
     * @throws RuntimeException if registration fails
     */
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

    /**
     * Updates an existing member's information.
     * Validates the member ID and updated data before performing the update.
     *
     * @param id the ID of the member to update
     * @param updated the member object containing updated information
     * @return the updated member object
     * @throws IllegalArgumentException if member ID or updated data is invalid
     * @throws IllegalStateException if member with given ID is not found
     * @throws RuntimeException if update operation fails
     */
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

    /**
     * Searches for members by name or contact information.
     *
     * @param query the search query string
     * @return list of members matching the search criteria
     * @throws RuntimeException if search fails
     */
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

    /**
     * Retrieves all members from the database.
     * Logs the operation and handles any exceptions that may occur.
     *
     * @return list of all members
     * @throws RuntimeException if retrieval fails
     */
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

    /**
     * Retrieves a specific member by their ID.
     * Validates the member ID and handles not found scenarios.
     *
     * @param id the ID of the member to retrieve
     * @return the member object if found, null otherwise
     * @throws IllegalArgumentException if member ID is invalid
     * @throws RuntimeException if retrieval fails
     */
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

    /**
     * Deletes a member from the library system.
     * Validates the member ID before attempting deletion.
     *
     * @param id the ID of the member to delete
     * @throws IllegalArgumentException if member ID is invalid
     * @throws RuntimeException if deletion fails
     */
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
