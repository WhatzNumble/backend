package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.category.domain.SubCategory;
import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Long showId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    @OneToMany(mappedBy = "videos")
    private List<Favorite> favorites = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private SubCategory subCategory;

    public Videos(String title, String content, Member member, Thumbnail thumbnail, Long showId) {
        this.videoLike = 0;
        this.videoViews = 0L;
        this.videoCreationDate = LocalDateTime.now();
        this.videoTitle = title;
        this.videoContent = content;
        this.member = member;
        this.thumbnail = thumbnail;
        this.showId = showId;
        member.addVideo(this);
    }

    public void modify(String title, String content, String cutName, String executeName) {
        this.videoTitle = title;
        this.videoContent = content;
        this.thumbnail.modify(cutName, executeName);
    }

    public void addFavorites(Favorite favorite) {
        this.favorites.add(favorite);
    }

    public void changeShowId(Long showId) {
        this.showId = showId;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }
}
