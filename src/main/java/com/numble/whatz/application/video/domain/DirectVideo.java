package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("direct")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectVideo extends Videos {

    private String directDir;

    @Builder
    public DirectVideo(String title, String content, Member member, String directDir, Thumbnail thumbnail, Long showId) {
        super(title, content, member, thumbnail, showId);
        this.directDir = directDir;
    }

    public void modify(String modifyVideo, String title, String content, String cutName, String executeName) {
        this.directDir = modifyVideo;
        modify(title, content, cutName, executeName);
    }
}
