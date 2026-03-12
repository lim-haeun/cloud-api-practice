package com.haeun.apipractice.member.dto;

import lombok.Builder;

@Builder
public record ProfileImageUploadResponse(
        Long memberId,
        String key,
        String profileImageUrl
) {
}
