package com.haeun.apipractice.member.dto;

import com.haeun.apipractice.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
public record MemberResponse (
        Long id, String name, Integer age, String mbti
) {
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .age(member.getAge())
                .mbti(member.getMbti())
                .build();
}
}