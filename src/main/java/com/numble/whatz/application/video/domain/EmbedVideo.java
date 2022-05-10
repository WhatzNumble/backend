package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("embed")
public class EmbedVideo extends Videos {
    private String link;

    @Builder
    public EmbedVideo(String title, String content, Member member, String link, Thumbnail thumbnail) {
        super(title, content, member, thumbnail);
        this.link = link;
    }

    public void modify(String link, String title, String content, String cutName, String executeName) {
        this.link = link;
        modify(title, content, cutName, executeName);
    }
}
