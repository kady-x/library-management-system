package com.aiu.library.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aiu.library.datastructures.MemberBST;
import com.aiu.library.model.Member;
@Repository
public class MemberRepository {
    
     private static MemberBST memberList = new MemberBST();

    public void save(Member m) {
        memberList.insert(m);
    }
    public Member findById(int id) {
        return memberList.search(id);
    }
    
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
    
    public List<Member> findAll() {
        return memberList.listMembers();
    }

}
