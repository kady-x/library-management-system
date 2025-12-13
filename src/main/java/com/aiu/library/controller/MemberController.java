package com.aiu.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aiu.library.model.Member;
import com.aiu.library.service.MemberService;

/**
 * REST Controller for managing library member operations.
 * Provides endpoints for CRUD operations on members with consistent error handling.
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private final MemberService service;

    /**
     * Constructs a MemberController with the required MemberService dependency.
     *
     * @param service the service for member business logic operations
     */
    public MemberController(MemberService service) {
        this.service = service;
    }

    /**
     * Retrieves all members from the database.
     *
     * @return list of all members
     */
    @GetMapping
    public List<Member> getAllMembers() {
        return service.getAllMembers();
    }

    /**
     * Searches for members by name or contact information.
     *
     * @param query the search query string
     * @return list of members matching the search criteria
     */
    @GetMapping("/search")
    public List<Member> searchMembers(@RequestParam String query) {
        return service.searchMembers(query);
    }

    /**
     * Retrieves a specific member by their ID.
     *
     * @param id the ID of the member to retrieve
     * @return the member object if found
     */
    @GetMapping("/{id}")
    public Member getMember(@PathVariable int id) {
        return service.getMemberById(id);
    }

    /**
     * Registers a new member in the library system.
     *
     * @param member the member object to be registered
     * @return the registered member with generated ID
     */
    @PostMapping
    public Member addMember(@RequestBody Member member) {
        return service.registerMember(member);
    }

    /**
     * Updates an existing member's information.
     *
     * @param id the ID of the member to update
     * @param member the member object containing updated information
     * @return the updated member object
     */
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable int id, @RequestBody Member member) {
        return service.updateMember(id, member);
    }

    /**
     * Deletes a member from the library system.
     *
     * @param id the ID of the member to delete
     */
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        service.deleteMember(id);
    }
}
