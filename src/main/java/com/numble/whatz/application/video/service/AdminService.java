package com.numble.whatz.application.video.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.video.controller.dto.MainContentDetailDto;
import com.numble.whatz.application.video.controller.dto.MainContentsDto;
import com.numble.whatz.application.video.controller.dto.MainContentDto;
import com.numble.whatz.application.video.controller.dto.UserVideoDto;
import com.numble.whatz.application.video.controller.dto.UserVideosDto;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    public UserVideosDto getUserVideos(Long id, Pageable pageable) {
        Member member = getMember(id);
        Page<Videos> page = videoRepository.findByMemberWithPageable(member, pageable);
        List<UserVideoDto> userVideoDtos = page.map(videos -> UserVideoDto.builder()
                .videos(videos)
                .build()).toList();

        return new UserVideosDto(member.getId(), member.getEmail(), member.getNickName(), userVideoDtos);
    }

    public MainContentsDto getMainContent(Pageable pageable) {
        Page<Videos> page = videoRepository.findAll(pageable);
        List<MainContentDto> list = page.map(videos ->
                MainContentDto.builder()
                        .id(videos.getId())
                        .videoThumbnail(videos.getThumbnail().getCutFile())
                        .title(videos.getVideoTitle())
                        .nickName(videos.getMember().getNickName())
                        .build()).toList();
        return new MainContentsDto(list);
    }

    public MainContentDetailDto getDetail(Long videoId) {
        Videos videos = getVideos(videoId);
        MainContentDetailDto mainContentDetailDto = MainContentDetailDto.builder()
                .videoContent(videos.getVideoContent())
                .videoLike(videos.getVideoLike())
                .videoId(videoId)
                .showId(videos.getShowId())
                .videoThumbnail(videos.getThumbnail().getCutFile())
                .videoTitle(videos.getVideoTitle())
                .videoViews(videos.getVideoViews())
                .videoCreationDate(videos.getVideoCreationDate())
                .nickname(videos.getMember().getNickName())
                .userId(videos.getMember().getId())
                .userEmail(videos.getMember().getEmail())
                .build();
        mainContentDetailDto.addVideoUrl(videos);

        return mainContentDetailDto;
    }

    @Transactional
    public void removeVideo(Long videoId) {
        Videos videos = getVideos(videoId);
        List<Favorite> favorites = videos.getFavorites();

        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
        }
        videoRepository.delete(videos);
    }

    @Transactional
    public void modifyVideoId(Long videoId, String showId) {
        Videos videos = getVideos(videoId);
        Optional<Videos> findVideo = videoRepository.findByShowId(showId);
        if (findVideo.isEmpty()) videos.changeShowId(Long.parseLong(showId));
        else {
            Videos tmp = findVideo.get();
            tmp.changeShowId(videos.getShowId());
            videos.changeShowId(tmp.getShowId());
        }
    }

    private Member getMember(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isEmpty()) throw new IllegalStateException("해당 회원 존재하지 않습니다.");
        Member member = findMember.get();
        return member;
    }

    private Videos getVideos(long parseId) {
        Optional<Videos> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 비디오가 존재하지 않습니다.");
        Videos video = findVideo.get();
        return video;
    }
}
