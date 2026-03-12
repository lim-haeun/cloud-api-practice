package com.haeun.apipractice.member.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileImageDownloadResponse(
        Long memberId,
        String presignedUrl,
        LocalDateTime expiredAt
) {
}
