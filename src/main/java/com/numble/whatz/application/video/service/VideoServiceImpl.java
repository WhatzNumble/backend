package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
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
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveDirect(DirectDto video, Principal principal) throws IOException {
        String executeFileName = videoStore.storeVideo(video.getFile());

        Member member = getMember(principal);
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
                .member(member)
                .directDir(executeFileName)
                .build();

        videoRepository.save(direct);
    }

    @Override
    @Transactional
    public void saveEmbed(EmbedDto video, Principal principal) {
        Member member = getMember(principal);
        // ==== 썸네일 관련 ===== //
        Thumbnail thumbnail = Thumbnail.builder()
                .executeFile("execute")
                .originalFile("original")
                .build();
        // ==== 썸네일 관련 ==== //

        EmbedVideo embed = EmbedVideo.builder()
                .content(video.getContent())
                .thumbnail(thumbnail)
                .member(member)
                .title(video.getTitle())
                .link(video.getLink())
                .build();

        videoRepository.save(embed);
    }

    @Override
    public void removeVideo(String id, Principal principal) {
        long parseId = Long.parseLong(id);
        Videos video = getVideos(parseId);
        Member member = getMember(principal);
        checkOwner(video, member);
        videoRepository.delete(video);
    }

    @Override
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

    @Override
    public MyVideosDto getMyVideos(Pageable pageable, Principal principal) {
        Member member = getMember(principal);
        Page<Videos> page = videoRepository.findByMember(member, pageable);
        List<MyVideoDto> content =
                page.map(videos -> new MyVideoDto(videos.getId(), videos.getThumbnail().getExecuteFile())).getContent();
        return new MyVideosDto(content);
    }

    @Override
    public VideoInfoDto getOneVideo(Long id) {
        Videos videos = getVideos(id);
        VideoInfoDto videoInfoDto = getVideoInfoDto(videos);
        return videoInfoDto;
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

    private Videos getVideos(long parseId) {
        Optional<Videos> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 비디오가 존재하지 않습니다.");
        Videos video = findVideo.get();
        return video;
    }

    private Member getMember(Principal principal) {
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());
        if (findMember.isEmpty()) throw new IllegalStateException("해당 영상이 존재하지 않습니다.");
        Member member = findMember.get();
        return member;
    }

    private void checkOwner(Videos video, Member member) {
        if (!video.getMember().getId().equals(member.getId())) throw new IllegalStateException("회원이 영상 주인이 아닙니다.");
    }
}
