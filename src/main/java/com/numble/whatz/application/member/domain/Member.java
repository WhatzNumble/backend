package com.numble.whatz.application.member.domain;

import com.numble.whatz.application.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue
    private Long id;
    private String email;
    private String nickName;
    private String profilePath;
    private String snsId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String nickName, String profilePath, String snsId, Role role) {
        this.email = email;
        this.nickName = nickName;
        this.profilePath = profilePath;
        this.snsId = snsId;
        this.role = role;
    }
}
