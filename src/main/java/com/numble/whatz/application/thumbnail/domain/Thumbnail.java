package com.numble.whatz.application.thumbnail.domain;

import com.numble.whatz.application.video.domain.Videos;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thumbnail {

    @Id
    @GeneratedValue
    @Column(name = "thumbnail_id")
    private Long id;
    private String cutFile;
    private String executeFile;

    @OneToOne(mappedBy = "thumbnail",fetch = FetchType.LAZY)
    private Videos video;

    @Builder
    public Thumbnail(String cutFile, String executeFile) {
        this.cutFile = cutFile;
        this.executeFile = executeFile;
    }

    public void modify(String cutName, String executeName) {
        this.cutFile = cutName;
        this.executeFile = executeName;
    }
}
