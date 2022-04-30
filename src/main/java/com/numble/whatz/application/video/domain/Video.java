package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import lombok.AccessLevel;
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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
