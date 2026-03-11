package com.haeun.apipractice.member.controller;

import com.haeun.apipractice.member.dto.CreateMemberRequest;
import com.haeun.apipractice.member.dto.MemberResponse;
import com.haeun.apipractice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 팀원 정보 저장
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(
            @Valid @RequestBody CreateMemberRequest request
    ) {
        MemberResponse response = memberService.createMember(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 팀원 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id) {

        MemberResponse response = memberService.getMember(id);

        return ResponseEntity.ok(response);
    }
}
