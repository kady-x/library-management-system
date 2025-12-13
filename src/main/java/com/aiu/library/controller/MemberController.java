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
import com.aiu.library.model.MemberWithBillingRequest;
import com.aiu.library.service.MemberService;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<Member> getAllMembers() {
        return service.getAllMembers();
    }

    @GetMapping("/search")
    public List<Member> searchMembers(@RequestParam String query) {
        return service.searchMembers(query);
    }

    @GetMapping("/{id}")
    public Member getMember(@PathVariable int id) {
        return service.getMemberById(id);
    }

    @PostMapping
    public Member addMember(@RequestBody Object request) {
        if (request instanceof MemberWithBillingRequest) {
            return service.registerMemberWithBilling((MemberWithBillingRequest) request);
        } else if (request instanceof Member) {
            return service.registerMember((Member) request);
        } else {
            throw new IllegalArgumentException("Unsupported request type");
        }
    }

    
    @PutMapping("/{id}")
    public Member updateMember(@PathVariable int id, @RequestBody Object request) {
        if (request instanceof MemberWithBillingRequest) {
            return service.updateMemberWithBilling(id, (MemberWithBillingRequest) request);
        } else if (request instanceof Member) {
            return service.updateMember(id, (Member) request);
        } else {
            throw new IllegalArgumentException("Unsupported request type");
        }
    }

    
    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        service.deleteMember(id);
    }
}
