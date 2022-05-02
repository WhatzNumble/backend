package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("embed")
public class Embed extends Video{
    private String link;

    @Builder
    public Embed(String title, String content, Member member, String link, Thumbnail thumbnail) {
        super(title, content, member, thumbnail);
        this.link = link;
    }
}
