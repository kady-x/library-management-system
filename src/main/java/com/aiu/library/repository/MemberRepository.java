package com.aiu.library.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aiu.library.datastructures.MemberList;
import com.aiu.library.model.Member;
@Repository
public class MemberRepository {
     private static MemberList memberList = new MemberList();

    // Save new member
    public void save(Member m) {
        memberList.insert(m);
    }
    // Search by ID
    public Member findById(int id) {
        return memberList.search(id);
    }
    
    // Generate next ID
    public static Integer generateNextId() {
        Integer maxId = null;
        List<Member> allMembers = memberList.listMembers();
        if (!allMembers.isEmpty()) {
            maxId = allMembers.stream()
                    .mapToInt(Member::getMemberId)
                    .max()
                    .orElse(0);
        }
        return (maxId == null) ? 1 : maxId + 1;
    }
    
    // Return all members 
    public List<Member> findAll() {
        return memberList.listMembers();
    }

}
