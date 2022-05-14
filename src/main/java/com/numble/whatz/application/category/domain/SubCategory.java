package com.numble.whatz.application.category.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.video.domain.Videos;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
public class SubCategory {

    @Id
    @GeneratedValue
    @Column(name = "sub_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Videos videos;

    public SubCategory() {
    }

    public SubCategory(Category category, Member member) {
        this.category = category;
        this.member = member;
        this.member.getCategories().add(this);
    }

    public SubCategory(Category category, Videos videos) {
        this.category = category;
        this.videos = videos;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
