package com.haeun.apipractice.member.service;

import com.haeun.apipractice.global.exception.MemberNotFoundException;
import com.haeun.apipractice.member.dto.CreateMemberRequest;
import com.haeun.apipractice.member.dto.MemberResponse;
import com.haeun.apipractice.member.dto.ProfileImageDownloadResponse;
import com.haeun.apipractice.member.dto.ProfileImageUploadResponse;
import com.haeun.apipractice.member.entity.Member;
import com.haeun.apipractice.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import io.awspring.cloud.s3.S3Template;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    // Presigned URL 유효기간을 7일로 고정
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);

    private final MemberRepository memberRepository;
    private final S3Template s3Template;

    // application.yml에 설정한 S3 버킷 이름 주입
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // application.yml에 설정한 AWS 리전 값 주입
    @Value("${spring.cloud.aws.region.static}")
    private String region;

    // 팀원 정보를 생성하고 DB에 저장한 뒤 응답 DTO로 변환
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
    // id로 팀원을 조회하고, 없으면 예외를 발생시킨 뒤 응답 DTO로 변환
    public MemberResponse getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 팀원입니다."));

        return MemberResponse.from(member);
    }
    // 프로필 이미지를 S3에 업로드하고, 업로드한 파일의 key와 url을 DB에 저장
    @Transactional
    public ProfileImageUploadResponse uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 팀원입니다."));

        // 비어 있는 파일인지, 이미지 파일인지 검증
        validateImage(file);

        // S3에 저장할 고유한 파일 key 생성
        String key = createKey(memberId, file.getOriginalFilename());

        try {
            // S3 버킷에 실제 파일 업로드
            s3Template.upload(bucket, key, file.getInputStream());

            // 저장된 파일의 기본 접근 URL 생성
            String profileImageUrl = buildFileUrl(key);
            // Member 엔티티에 프로필 이미지 key와 url 저장
            member.updateProfileImage(key, profileImageUrl);

            // 업로드 결과를 응답 DTO로 반환
            return ProfileImageUploadResponse.builder()
                    .memberId(memberId)
                    .key(key)
                    .profileImageUrl(profileImageUrl)
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    // 저장된 프로필 이미지 key를 이용해 7일짜리 Presigned URL을 생성해서 반환
    public ProfileImageDownloadResponse getProfileImageDownloadUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 팀원입니다."));

        // 해당 회원의 프로필 이미지가 등록되지 않은 경우 예외 처리
        if (member.getProfileImageKey() == null || member.getProfileImageKey().isBlank()) {
            throw new IllegalArgumentException("프로필 이미지가 없습니다.");
        }

        // S3에서 다운로드 가능한 Presigned URL 생성
        URL url = s3Template.createSignedGetURL(
                bucket,
                member.getProfileImageKey(),
                PRESIGNED_URL_EXPIRATION
        );

        // 생성된 URL과 만료 시각을 응답 DTO로 반환
        return ProfileImageDownloadResponse.builder()
                .memberId(memberId)
                .presignedUrl(url.toString())
                .expiredAt(LocalDateTime.now().plusDays(7))
                .build();
    }

    // 업로드된 파일이 비어 있지 않은지, 이미지 타입인지 검증
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어 있습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    // S3에 저장할 파일명을 회원 id + UUID 기반으로 생성
    private String createKey(Long memberId, String originalFilename) {
        String extension = extractExtension(originalFilename);
        return "members/" + memberId + "/" + UUID.randomUUID() + "." + extension;
    }

    // 원본 파일명에서 확장자를 추출하고, 없으면 기본값 jpg 사용
    private String extractExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "jpg";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
    }

    // 업로드된 파일의 S3 기본 접근 URL 문자열 생성
    private String buildFileUrl(String key) {
        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}
