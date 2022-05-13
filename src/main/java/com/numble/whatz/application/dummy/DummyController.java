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
                .role(Role.MEMBER)
                .snsId("snsId1")
                .build();

        Member member2 = Member.builder()
                .email("email2")
                .nickName("user2")
                .thumbnailUrl("profile2")
                .role(Role.MEMBER)
                .snsId("snsId2")
                .build();

        Member member3 = Member.builder()
                .email("email3")
                .nickName("user3")
                .thumbnailUrl("profile3")
                .role(Role.MEMBER)
                .snsId("snsId3")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        DirectVideo direct1 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .cutFile("/WhatzDev/thumbnail/c95d7879-0f99-44c6-9ad3-acd6251db537/c95d7879-0f99-44c6-9ad3-acd6251db537_CUT.jpeg")
                        .executeFile("/WhatzDev/thumbnail/c95d7879-0f99-44c6-9ad3-acd6251db537/c95d7879-0f99-44c6-9ad3-acd6251db537.jpeg")
                        .build())
                .content("content1")
                .member(member1)
                .title("title1")
                .showId(0L)
                .directDir("/WhatzDev/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4/d6fb2d78-a6e6-4c11-ab91-f7d00dac52d4.m3u8")
                .build();

        DirectVideo direct2 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .cutFile("/WhatzDev/thumbnail/1eb39e24-d7f2-4794-950d-2e9cec5361ef/1eb39e24-d7f2-4794-950d-2e9cec5361ef_CUT.jpg")
                        .executeFile("/WhatzDev/thumbnail/1eb39e24-d7f2-4794-950d-2e9cec5361ef/1eb39e24-d7f2-4794-950d-2e9cec5361ef.jpg")
                        .build())
                .content("content2")
                .member(member1)
                .title("title2")
                .showId(1L)
                .directDir("/WhatzDev/ffaf8b8e-7df5-468c-80b2-fe4cba007be4/ffaf8b8e-7df5-468c-80b2-fe4cba007be4.m3u8")
                .build();

        DirectVideo direct3 = DirectVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .cutFile("/WhatzDev/thumbnail/6b85d54c-3391-4393-b074-1ecafacd969b/6b85d54c-3391-4393-b074-1ecafacd969b_CUT.png")
                        .executeFile("/WhatzDev/thumbnail/6b85d54c-3391-4393-b074-1ecafacd969b/6b85d54c-3391-4393-b074-1ecafacd969b.png")
                        .build())
                .content("content3")
                .member(member2)
                .title("title3")
                .showId(2L)
                .directDir("/WhatzDev/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516/5ebfbf21-4a4b-4361-b00b-d03d3b1d6516.m3u8")
                .build();

        EmbedVideo embed1 = EmbedVideo.builder()
                .thumbnail(Thumbnail.builder()
                        .cutFile("/WhatzDev/thumbnail/2d1f1d9e-7fb0-4189-ae03-79a07494dc73/2d1f1d9e-7fb0-4189-ae03-79a07494dc73_CUT.jpeg")
                        .executeFile("/WhatzDev/thumbnail/2d1f1d9e-7fb0-4189-ae03-79a07494dc73/2d1f1d9e-7fb0-4189-ae03-79a07494dc73.jpeg")
                        .build())
                .title("title4")
                .link("https://www.youtube.com/watch?v=_whaAD__3vI")
                .member(member2)
                .showId(3L)
                .content("content4")
                .build();

        videoRepository.save(direct1);
        videoRepository.save(direct2);
        videoRepository.save(direct3);
        videoRepository.save(embed1);

    }
}
