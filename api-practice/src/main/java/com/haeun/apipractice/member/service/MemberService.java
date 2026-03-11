package com.haeun.apipractice.member.service;

import com.haeun.apipractice.global.exception.MemberNotFoundException;
import com.haeun.apipractice.member.dto.CreateMemberRequest;
import com.haeun.apipractice.member.dto.MemberResponse;
import com.haeun.apipractice.member.entity.Member;
import com.haeun.apipractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 팀원 정보 저장
    @Transactional
    public MemberResponse createMember(CreateMemberRequest request) {
        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );

        Member savedMember = memberRepository.save(member);
        return MemberResponse.from(savedMember);
    }

    // 팀원 정보 조회
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 팀원입니다."));

        return MemberResponse.from(member);
    }
}
