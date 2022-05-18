package com.numble.whatz.application.like.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import com.numble.whatz.application.video.controller.dto.VideoInfoDto;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import com.numble.whatz.application.video.service.VideoViewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public boolean toggleFavorite(Long id, Principal principal) {
        Member member = getMember(principal);
        Videos videos = getVideos(id);
        List<Favorite> favorites = member.getFavorites();
        for (Favorite favorite : favorites) {
            if (favorite.getVideos().equals(videos)) {
                favoriteRepository.delete(favorite);
                return false;
            }
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .videos(videos)
                .build();

        favoriteRepository.save(favorite);
        return true;
    }

    public HomeDto getFavoriteVideos(Pageable pageable, Principal principal) {
        Member member = getMember(principal);
        Page<Favorite> favorites = favoriteRepository.findByMemberId(member.getId(), pageable);
        Page<Videos> page = favorites.map(favorite -> favorite.getVideos());
        List<VideoInfoDto> videoInfoDtos = page.map(videos -> new VideoInfoDto(
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

    private Videos getVideos(Long id) {
        Optional<Videos> findVideo = videoRepository.findById(id);
        if (findVideo.isEmpty()) throw new IllegalStateException("해당 영상이 존재하지 않습니다.");
        Videos videos = findVideo.get();
        return videos;
    }

    private Member getMember(Principal principal) {
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());
        if (findMember.isEmpty()) throw new IllegalStateException("회원이 존재하지 않습니다.");
        return findMember.get();
    }
}
