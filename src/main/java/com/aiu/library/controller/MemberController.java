package com.aiu.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aiu.library.model.Member;
import com.aiu.library.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public Member addMember(@RequestBody Member m) {
        return service.registerMember(m);
    }

    // Get member by ID
    @GetMapping("/{id}")
    public Member getMember(@PathVariable int id) {
        return service.getMemberById(id);
    }

    // List all members (sorted)
    @GetMapping("/all")
    public List<Member> listMembers() {
        return service.getAllMembers();
    }

    // Update contact info
    @PutMapping("/edit/{id}")
    public String updateContact(@PathVariable int id, @RequestBody String newContact) {
        boolean updated = service.updateContact(id, newContact);

        return updated ? "Contact updated successfully."
                       : "Member not found.";
    }
}
