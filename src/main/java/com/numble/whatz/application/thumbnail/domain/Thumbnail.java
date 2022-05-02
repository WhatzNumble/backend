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
    private String originalFile;
    private String executeFile;

    @OneToOne(mappedBy = "thumbnail",fetch = FetchType.LAZY)
    private Videos video;

    @Builder
    public Thumbnail(String originalFile, String executeFile) {
        this.originalFile = originalFile;
        this.executeFile = executeFile;
    }
}
