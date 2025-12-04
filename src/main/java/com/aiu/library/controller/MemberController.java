package com.aiu.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import com.aiu.library.service.MemberService;
import com.aiu.library.model.Member;
import java.util.List;

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

    @GetMapping("/{id}")
    public Member getMember(@PathVariable int id) {
        return service.getMemberById(id);
    }

    @PostMapping
    public void addMember(@RequestBody Member member) {
        service.registerMember(member);
    }

    @PutMapping("/{id}")
    public void updateMember(@PathVariable int id, @RequestBody Member member) {
        service.updateMember(id, member);
    }

    @DeleteMapping("/{id}")
    public void deleteMember(@PathVariable int id) {
        service.deleteMember(id);
    }
}
