package com.aiu.library.service;

import com.aiu.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import com.aiu.library.model.Member;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public void registerMember(Member member) {
        if (member.getMemberId() == null) {
            member.setMemberId(memberRepository.generateNextId());
        } else if (memberRepository.findById(member.getMemberId()) != null) {
            throw new RuntimeException("Member ID already exists.");
        }
        memberRepository.insert(member);
    }
    
    public void updateMember(Integer id, Member updated) {
        Member existing = memberRepository.findById(id);
        if (existing == null) throw new RuntimeException("Member not found.");

        existing.setName(updated.getName());
        existing.setContactInfo(updated.getContactInfo());
        existing.setBorrowedBooks(updated.getBorrowedBooks());
        existing.setMembershipDate(updated.getMembershipDate());
        memberRepository.update(existing);
    }

    public Member getMemberById(int id) {
        return memberRepository.findById(id);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public void deleteMember(Integer id) {
        memberRepository.deleteById(id);
    }
}
