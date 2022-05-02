package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.member.service.MemberService;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.video.controller.dto.DirectDto;
import com.numble.whatz.application.video.controller.dto.EmbedDto;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService{

    private final VideoRepository videoRepository;
    private final VideoStore videoStore;
    private final MemberService memberService;

    @Override
    @Transactional
    public void saveDirect(DirectDto video, Principal principal) throws IOException {
        String executeFileName = videoStore.storeVideo(video.getFile());

        // 나중에 MemberService 로 교체
        Member findMember = memberService.getMemberBySnsId(principal.getName());
        // ==== 썸네일 관련 ===== //
        Thumbnail thumbnail = Thumbnail.builder()
                .executeFile("execute")
                .originalFile("original")
                .build();
        // ==== 썸네일 관련 ==== //

        DirectVideo direct = DirectVideo.builder()
                .thumbnail(thumbnail)
                .content(video.getContent())
                .title(video.getTitle())
                .member(findMember)
                .directDir(executeFileName)
                .build();

        videoRepository.save(direct);
    }

    @Override
    @Transactional
    public void saveEmbed(EmbedDto video, Principal principal) {
        Member findMember = memberService.getMemberBySnsId(principal.getName());
        // ==== 썸네일 관련 ===== //
        Thumbnail thumbnail = Thumbnail.builder()
                .executeFile("execute")
                .originalFile("original")
                .build();
        // ==== 썸네일 관련 ==== //

        EmbedVideo embed = EmbedVideo.builder()
                .content(video.getContent())
                .thumbnail(thumbnail)
                .member(findMember)
                .title(video.getTitle())
                .link(video.getLink())
                .build();

        videoRepository.save(embed);
    }

    @Override
    public void removeVideo(String id, Principal principal) {
        long parseId = Long.parseLong(id);
        Optional<Videos> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 비디오가 존재하지 않습니다.");
        Videos video = findVideo.get();
        Member member = memberService.getMemberBySnsId(principal.getName());
        if (!video.getMember().getId().equals(member.getId())) throw new IllegalStateException("회원이 영상 주인이 아닙니다.");
        videoRepository.delete(video);
    }

    @Override
    public Videos getVideoById(Long id) {
        Optional<Videos> findVideo = videoRepository.findById(id);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 영상이 존재하지 않습니다.");
        return findVideo.get();
    }

    @Override
    public HomeDto findAll(Pageable pageable, Principal principal) {
        Page<Videos> findVideos = videoRepository.findAll(pageable);
        List<VideoInfoDto> videos = new ArrayList<>();

        for (Videos findVideo : findVideos) {
            VideoInfoDto videoInfoDto = VideoInfoDto.builder()
                    .content(findVideo.getVideoContent())
                    .title(findVideo.getVideoTitle())
                    .views(findVideo.getVideoViews())
                    .videoDate(findVideo.getVideoCreationDate())
                    .nickname(findVideo.getMember().getNickName())
                    .likes(findVideo.getVideoLike())
                    .profile(findVideo.getMember().getProfilePath())
                    .build();
            if (findVideo instanceof DirectVideo) videoInfoDto.setDirectDir(((DirectVideo) findVideo).getDirectDir());
            else videoInfoDto.setEmbedLink(((EmbedVideo) findVideo).getLink());
            videos.add(videoInfoDto);
        }
        HomeDto homeDto = new HomeDto(videos, null);
        if (principal != null) {
            Member findMember = memberService.getMemberBySnsId(principal.getName());
            List<Favorite> favorites = findMember.getFavorites();
            List<Long> likeList = new ArrayList<>();
            for (Favorite favorite : favorites) {
                likeList.add(favorite.getVideo().getId());
            }
            homeDto.setLikeList(likeList);
        }
        return homeDto;
    }
}
