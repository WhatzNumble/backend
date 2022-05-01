package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Video {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;
    private LocalDateTime creationDate;
    private Integer like;
    private Long views;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    public Video(String title, String content, Member member, Thumbnail thumbnail) {
        this.like = 0;
        this.views = 0L;
        this.creationDate = LocalDateTime.now();
        this.title = title;
        this.content = content;
        this.member = member;
        this.thumbnail = thumbnail;
    }
}
