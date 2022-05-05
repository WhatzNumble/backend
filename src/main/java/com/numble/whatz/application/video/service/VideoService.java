package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreDto;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreExUtil;
import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
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
public class VideoService {

    private final VideoRepository videoRepository;
    private final VideoStore videoStore;
    private final MemberRepository memberRepository;
    private final ThumbnailStoreExUtil thumbnailStore;

    @Transactional
    public void saveDirect(DirectDto video, Principal principal) throws VideoStoreException, ThumbnailStoreException {
        Member member = getMember(principal);

        String executeFileName = videoStore.storeVideo(video.getFile());

        ThumbnailStoreDto storeThumbnail = thumbnailStore.storeThumbnail(video.getVideoThumbnail());

        DirectVideo direct = getDirectVideo(video, executeFileName, member, storeThumbnail);

        videoRepository.save(direct);
    }

    @Transactional
    public void saveEmbed(EmbedDto video, Principal principal) throws ThumbnailStoreException {
        Member member = getMember(principal);

        ThumbnailStoreDto storeThumbnail = thumbnailStore.storeThumbnail(video.getVideoThumbnail());

        EmbedVideo embed = getEmbedVideo(video, member, storeThumbnail);

        videoRepository.save(embed);
    }

    @Transactional
    public void removeVideo(String id, Principal principal) {
        long parseId = Long.parseLong(id);
        Videos video = getVideos(parseId);
        Member member = getMember(principal);
        checkOwner(video, member);

        if (video instanceof DirectVideo) videoStore.deleteVideo(((DirectVideo)video).getDirectDir());
        thumbnailStore.removeThumbnail(video.getThumbnail());

        videoRepository.delete(video);
    }

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
        Page<Videos> page = videoRepository.findByMember(member, pageable);
        List<MyVideoDto> content =
                page.map(videos -> new MyVideoDto(videos.getId(), videos.getThumbnail().getExecuteFile())).getContent();
        return new MyVideosDto(content);
    }

    public VideoInfoDto getOneVideo(Long id) {
        Videos videos = getVideos(id);
        VideoInfoDto videoInfoDto = getVideoInfoDto(videos);
        return videoInfoDto;
    }

    @Transactional
    public void modifyDirect(ModifyDirectDto video, Principal principal) throws VideoStoreException, ThumbnailStoreException {
        Member member = getMember(principal);
        Videos videos = getVideos(video.getId());
        checkOwner(videos, member);
        String modifyVideo = videoStore.modifyVideo(video.getFile(), ((DirectVideo) videos).getDirectDir());

        ThumbnailStoreDto thumbnail = thumbnailStore.modifyThumbnail(video.getVideoThumbnail(), videos.getThumbnail());

        ((DirectVideo) videos).modify(modifyVideo, video.getTitle(),
                video.getContent(), thumbnail.getCutName(), thumbnail.getExecuteName());
    }

    @Transactional
    public void modifyEmbed(ModifyEmbedDto video, Principal principal) throws ThumbnailStoreException {
        Member member = getMember(principal);
        Videos videos = getVideos(video.getId());
        checkOwner(videos, member);

        ThumbnailStoreDto thumbnail = thumbnailStore.modifyThumbnail(video.getVideoThumbnail(), videos.getThumbnail());

        ((EmbedVideo) videos).modify(video.getLink(), video.getTitle(),
                video.getContent(), thumbnail.getCutName(), thumbnail.getExecuteName());
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

    private DirectVideo getDirectVideo(DirectDto video, String executeFileName, Member member, ThumbnailStoreDto storeThumbnail) {
        Thumbnail thumbnail = Thumbnail.builder()
                .cutFile(storeThumbnail.getCutName())
                .executeFile(storeThumbnail.getExecuteName())
                .build();

        DirectVideo direct = DirectVideo.builder()
                .thumbnail(thumbnail)
                .content(video.getContent())
                .title(video.getTitle())
                .member(member)
                .directDir(executeFileName)
                .build();
        return direct;
    }

    private EmbedVideo getEmbedVideo(EmbedDto video, Member member, ThumbnailStoreDto storeThumbnail) {
        Thumbnail thumbnail = Thumbnail.builder()
                .cutFile(storeThumbnail.getCutName())
                .executeFile(storeThumbnail.getExecuteName())
                .build();

        EmbedVideo embed = EmbedVideo.builder()
                .content(video.getContent())
                .thumbnail(thumbnail)
                .member(member)
                .title(video.getTitle())
                .link(video.getLink())
                .build();
        return embed;
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

    private void checkOwner(Videos video, Member member) {
        if (!video.getMember().getId().equals(member.getId())) throw new IllegalStateException("회원이 영상 주인이 아닙니다.");
    }
}
