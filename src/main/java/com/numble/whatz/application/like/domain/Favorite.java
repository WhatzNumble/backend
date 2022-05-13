package com.numble.whatz.application.like.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.video.domain.Videos;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Videos video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Favorite(Videos video, Member member) {
        this.video = video;
        this.member = member;
        member.addFavorites(this);
        video.addFavorites(this);
    }
}
