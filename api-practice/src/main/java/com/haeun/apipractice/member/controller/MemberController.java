package com.haeun.apipractice.member.controller;

import com.haeun.apipractice.member.dto.CreateMemberRequest;
import com.haeun.apipractice.member.dto.MemberResponse;
import com.haeun.apipractice.member.dto.ProfileImageDownloadResponse;
import com.haeun.apipractice.member.dto.ProfileImageUploadResponse;
import com.haeun.apipractice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 프로필 이미지 업로드
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageUploadResponse> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        ProfileImageUploadResponse response = memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok(response);
    }

    // 프로필 이미지 조회
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<ProfileImageDownloadResponse> getProfileImage(
            @PathVariable Long id
    ) {
        ProfileImageDownloadResponse response = memberService.getProfileImageDownloadUrl(id);
        return ResponseEntity.ok(response);
    }
}
