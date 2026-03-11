package com.haeun.apipractice.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotNull(message = "나이는 필수입니다.")
    private Integer age;
    @NotBlank(message = "mbti는 필수입니다.")
    private String mbti;
}