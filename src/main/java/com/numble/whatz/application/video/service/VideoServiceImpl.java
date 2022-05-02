package com.numble.whatz.application.video.service;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.video.controller.dto.DirectDto;
import com.numble.whatz.application.video.controller.dto.EmbedDto;
import com.numble.whatz.application.video.controller.dto.MyVideosDto;
import com.numble.whatz.application.video.domain.Direct;
import com.numble.whatz.application.video.domain.Embed;
import com.numble.whatz.application.video.domain.Video;
import com.numble.whatz.application.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService{

    private final VideoRepository videoRepository;
    private final VideoStore videoStore;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveDirect(DirectDto video, Principal principal) throws IOException {
        String executeFileName = videoStore.storeVideo(video.getFile());

        // 나중에 MemberService 로 교체
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());
        // ==== 썸네일 관련 ===== //
        Thumbnail thumbnail = Thumbnail.builder()
                .executeFile("execute")
                .originalFile("original")
                .build();
        // ==== 썸네일 관련 ==== //

        Direct direct = Direct.builder()
                .directDir(executeFileName)
                .content(video.getContent())
                .title(video.getTitle())
                .member(findMember.get())
                .thumbnail(thumbnail)
                .build();
        videoRepository.save(direct);
    }

    @Override
    @Transactional
    public void saveEmbed(EmbedDto video, Principal principal) {
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());

        // ==== 썸네일 관련 ===== //
        Thumbnail thumbnail = Thumbnail.builder()
                .executeFile("execute")
                .originalFile("original")
                .build();
        // ==== 썸네일 관련 ==== //

        Embed embed = Embed.builder()
                .content(video.getContent())
                .link(video.getLink())
                .member(findMember.get())
                .thumbnail(thumbnail)
                .title(video.getTitle())
                .build();

        videoRepository.save(embed);
    }

    @Override
    public void removeVideo(String id, Principal principal) {
        long parseId = Long.parseLong(id);
        Optional<Video> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 비디오가 존재하지 않습니다.");
        Video video = findVideo.get();
        Member member = memberRepository.findBySnsId(principal.getName()).get();
        if (!video.getMember().getId().equals(member.getId())) throw new IllegalStateException("회원이 영상 주인이 아닙니다.");
        videoRepository.delete(video);
    }
}
