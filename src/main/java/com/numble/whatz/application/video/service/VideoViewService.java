package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.video.controller.dto.*;
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
import java.util.stream.Collectors;

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
                likeList.add(favorite.getVideos().getId());
            }
            homeDto.setLikeList(likeList);
        }
        return homeDto;
    }

    public HomeDto getMyVideos(Pageable pageable, Principal principal) {
        Member member = getMember(principal);
        Page<Videos> page = videoRepository.findByMemberWithPageable(member, pageable);
        List<VideoInfoDto> videoInfoDtos = page.map(videos -> new VideoInfoDto(
                videos.getSubCategory().getCategory().getName(),
                videos.getId(),
                member.getNickName(),
                member.getThumbnailUrl(),
                videos.getVideoLike(),
                videos.getVideoTitle(),
                videos.getVideoContent(),
                videos.getVideoCreationDate(),
                videos.getVideoViews(),
                videos)).toList();
        List<Long> likeList = member.getFavorites()
                .stream().map(Favorite::getVideos).collect(Collectors.toList())
                .stream().map(Videos::getId).collect(Collectors.toList());

        return new HomeDto(videoInfoDtos, likeList);
    }

    public VideoDetailDto getOneVideo(Long id) {
        Videos videos = getVideos(id);
        VideoDetailDto videoDetailDto = getVideoDetailDto(videos);
        return videoDetailDto;
    }

    private VideoDetailDto getVideoDetailDto(Videos videos) {
        VideoDetailDto videoDetailDto = VideoDetailDto.builder()
                .videoId(videos.getId())
                .videoContent(videos.getVideoContent())
                .videoTitle(videos.getVideoTitle())
                .videoViews(videos.getVideoViews())
                .videoCreationDate(videos.getVideoCreationDate())
                .nickname(videos.getMember().getNickName())
                .videoLike(videos.getVideoLike())
                .profile(videos.getMember().getThumbnailUrl())
                .videoThumbnail(videos.getThumbnail().getCutFile())
                .build();
        if (videos instanceof DirectVideo) videoDetailDto.setDirectDir(((DirectVideo) videos).getDirectDir());
        else videoDetailDto.setEmbedLink(((EmbedVideo) videos).getLink());
        return videoDetailDto;
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
                .videoId(videos.getId())
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
