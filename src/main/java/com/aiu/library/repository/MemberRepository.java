package com.aiu.library.repository;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Repository;
import com.aiu.library.datastructures.MemberBST;
import com.aiu.library.model.Member;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepository {

    private static MemberBST memberList = new MemberBST();
    private final MemberJpaRepository memberJpaRepository;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private volatile boolean initialized = false;

    MemberRepository(MemberJpaRepository memberJpaRepository){
        this.memberJpaRepository = memberJpaRepository;
        initializeFromDatabase();
    }

    private void initializeFromDatabase() {
        if (initialized) return;

        Integer maxFromJpa = null;
        List<Member> all = memberJpaRepository.findAll();
        if (all != null && !all.isEmpty()) {
            for (Member m : all) {
                if (m != null) memberList.addMember(m);
            }

            try {
                maxFromJpa = memberJpaRepository.findMaxMemberId();
            } catch (Exception ex) {
                maxFromJpa = all.stream()
                        .map(Member::getMemberId)
                        .filter(id -> id != null)
                        .max(Integer::compareTo)
                        .orElse(null);
            }
        }
        Integer maxFromBst = memberList.findMaxId(memberList.getRoot());
        int max = 0;
        if (maxFromJpa != null) max = Math.max(max, maxFromJpa);
        if (maxFromBst != null) max = Math.max(max, maxFromBst);
        this.nextId.set(max + 1);

        initialized = true;
    }

    public void insert(Member member) {
        if (member.getMemberId() == null) {
            member.setMemberId(generateNextId());
        }
        Member saved = memberJpaRepository.save(member);
        memberList.addMember(saved);
    }

    public void update(Member member) {
        Member saved = memberJpaRepository.save(member);
        memberList.updateMemberInfo(saved);
    }

    public void deleteById(Integer id) {
        memberJpaRepository.deleteById(id);
        memberList.delete(id);
    }

    public synchronized Integer generateNextId() {
        return Integer.valueOf(nextId.getAndIncrement());
    }

    public Member findById(int id) {
        return memberList.searchByID(id);
    }

    public Member findById(Integer id) {
        return memberList.searchByID(id);
    }

    public Member findByName(String name) {
        return memberList.searchByName(name);
    }
    
    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        memberList.inOrderTraversal(memberList.getRoot(), members);
        return members;
    }

}
