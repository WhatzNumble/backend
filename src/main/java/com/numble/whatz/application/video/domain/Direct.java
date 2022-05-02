package com.numble.whatz.application.video.domain;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("direct")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Direct extends Video{
    private String directDir;

    @Builder
    public Direct(String title, String content, Member member, String directDir, Thumbnail thumbnail) {
        super(title, content, member, thumbnail);
        this.directDir = directDir;
    }
}
