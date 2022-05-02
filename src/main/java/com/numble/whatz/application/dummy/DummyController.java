package com.numble.whatz.application.dummy;

import com.numble.whatz.application.Role;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.repository.ThumbnailRepository;
import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DummyController {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final ThumbnailRepository thumbnailRepository;


    @PostConstruct
    public void dummyData() {
        Member member1 = Member.builder()
                .email("email1")
                .nickName("user1")
                .thumbnailUrl("profile1")
                .role(Role.ROLE_MEMBER)
                .snsId("snsId1")
                .build();

        Member member2 = Member.builder()
                .email("email2")
                .nickName("user2")
                .thumbnailUrl("profile2")
                .role(Role.ROLE_MEMBER)
                .snsId("snsId2")
                .build();

        Member member3 = Member.builder()
                .email("email3")
                .nickName("user3")
                .thumbnailUrl("profile3")
                .role(Role.ROLE_MEMBER)
                .snsId("snsId3")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        DirectVideo direct1 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .originalFile("original1")
                        .executeFile("execute1")
                        .build())
                .content("content1")
                .member(member1)
                .title("title1")
                .directDir("93210b1d-7c54-4208-84a3-c4bc97b02c64.m3u8")
                .build();

        DirectVideo direct2 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .originalFile("original2")
                        .executeFile("execute2")
                        .build())
                .content("content2")
                .member(member1)
                .title("title2")
                .directDir("38b571b8-c9e3-4b8e-b9a7-7f48dfd7dd5b.m3u8")
                .build();

        DirectVideo direct3 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .originalFile("original3")
                        .executeFile("execute3")
                        .build())
                .content("content3")
                .member(member2)
                .title("title3")
                .directDir("5dc04aa2-6297-4cce-aab6-a43ab761da65.m3u8")
                .build();

        EmbedVideo embed1 = EmbedVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .originalFile("original4")
                        .executeFile("execute4")
                        .build())
                .title("title4")
                .link("https://youtube.com/shorts/E4BR0sAM3-8?feature=share")
                .member(member2)
                .content("content4")
                .build();

        videoRepository.save(direct1);
        videoRepository.save(direct2);
        videoRepository.save(direct3);
        videoRepository.save(embed1);

    }
}
