package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.MyVideoDto;
import com.numble.whatz.application.video.controller.dto.MyVideosDto;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoViewService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    public HomeDto findAll(Pageable pageable, Principal principal) {
        Page<Videos> findVideos = videoRepository.findAll(pageable);
        List<VideoInfoDto> videos = new ArrayList<>();

        for (Videos findVideo : findVideos) {
            VideoInfoDto videoInfoDto = getVideoInfoDto(findVideo);
            videos.add(videoInfoDto);
        }
        HomeDto homeDto = new HomeDto(videos, null);
        if (principal != null) {
            Member member = getMember(principal);
            List<Favorite> favorites = member.getFavorites();
            List<Long> likeList = new ArrayList<>();
            for (Favorite favorite : favorites) {
                likeList.add(favorite.getVideo().getId());
            }
            homeDto.setLikeList(likeList);
        }
        return homeDto;
    }

    public MyVideosDto getMyVideos(Pageable pageable, Principal principal) {
        Member member = getMember(principal);
        Page<Videos> page = videoRepository.findByMemberWithPageable(member, pageable);
        List<MyVideoDto> content =
                page.map(videos -> new MyVideoDto(videos.getId(), videos.getThumbnail().getExecuteFile())).getContent();
        return new MyVideosDto(content);
    }

    public VideoInfoDto getOneVideo(Long id) {
        Videos videos = getVideos(id);
        VideoInfoDto videoInfoDto = getVideoInfoDto(videos);
        return videoInfoDto;
    }

    private Videos getVideos(long parseId) {
        Optional<Videos> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 비디오가 존재하지 않습니다.");
        Videos video = findVideo.get();
        return video;
    }

    private Member getMember(Principal principal) {
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());
        if (findMember.isEmpty()) throw new IllegalStateException("해당 회원 존재하지 않습니다.");
        Member member = findMember.get();
        return member;
    }

    private VideoInfoDto getVideoInfoDto(Videos videos) {
        VideoInfoDto videoInfoDto = VideoInfoDto.builder()
                .content(videos.getVideoContent())
                .title(videos.getVideoTitle())
                .views(videos.getVideoViews())
                .videoDate(videos.getVideoCreationDate())
                .nickname(videos.getMember().getNickName())
                .likes(videos.getVideoLike())
                .profile(videos.getMember().getThumbnailUrl())
                .build();
        if (videos instanceof DirectVideo) videoInfoDto.setDirectDir(((DirectVideo) videos).getDirectDir());
        else videoInfoDto.setEmbedLink(((EmbedVideo) videos).getLink());
        return videoInfoDto;
    }
}
