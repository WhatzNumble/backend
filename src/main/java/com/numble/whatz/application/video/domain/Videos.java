package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "VideoType")
public abstract class Videos {

    @Id
    @GeneratedValue
    @Column(name = "video_id")
    private Long id;
    private LocalDateTime videoCreationDate;
    private Integer videoLike;
    private Long videoViews;
    private String videoTitle;
    private String videoContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    public Videos(String title, String content, Member member, Thumbnail thumbnail) {
        this.videoLike = 0;
        this.videoViews = 0L;
        this.videoCreationDate = LocalDateTime.now();
        this.videoTitle = title;
        this.videoContent = content;
        this.member = member;
        this.thumbnail = thumbnail;
        member.addVideo(this);
    }

    public void modify(String title, String content, Thumbnail thumbnail) {
        this.videoTitle = title;
        this.videoContent = content;
        this.thumbnail = thumbnail;
    }
}
