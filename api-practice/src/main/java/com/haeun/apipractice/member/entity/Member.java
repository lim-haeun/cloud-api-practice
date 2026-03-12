package com.haeun.apipractice.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;
    // int로 하면 반드시 값이 있어야 하기 때문에 Integer 사용

    private String mbti;

    private String profileImageKey; // S3 객체 key 저장
    private String profileImageUrl; // S3 기본 URL 저장

    public Member(String name, Integer age, String mbti) {
        this.name = name;
        this.age = age;
        this.mbti = mbti;
    }

    public void updateProfileImage(String profileImageKey, String profileImageUrl) {
        this.profileImageKey = profileImageKey;
        this.profileImageUrl = profileImageUrl;
    }
}
