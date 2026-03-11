package com.haeun.apipractice.member.repository;

import com.haeun.apipractice.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
