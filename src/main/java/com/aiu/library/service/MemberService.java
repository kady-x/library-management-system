package com.aiu.library.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aiu.library.model.Member;
import com.aiu.library.repository.MemberRepository;
@Service
public class MemberService {
    private MemberRepository repo = new MemberRepository();

    // Register a new member
    public Member registerMember(Member m) {
        if (m.getMemberId() == 0) {
            m.setMemberId(MemberRepository.generateNextId());
        } else if (repo.findById(m.getMemberId()) != null) {
            throw new RuntimeException("Member ID already exists.");
        }
        repo.save(m);
        return m;
    }

    // Get member by ID
    public Member getMemberById(int id) {
        return repo.findById(id);
    }

    // Get all members
    public List<Member> getAllMembers() {
        return repo.findAll();
    }

    // Update contact info of a member
    public boolean updateContact(int id, String newContact) {
        Member m = repo.findById(id);

        if (m != null) {
            m.setContactInfo(newContact);
            return true;
        } else {
            return false;
        }
    }
}
