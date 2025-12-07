package com.aiu.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.aiu.library.model.Member;

@Repository
public interface MemberJpaRepository extends JpaRepository<Member, Integer> {
    @Query("SELECT COALESCE(MAX(m.memberId), 0) FROM Member m")
    Integer findMaxMemberId();
}
