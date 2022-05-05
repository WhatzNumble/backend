package com.numble.whatz.application.like.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.member.service.CrudMemberService;
import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import com.numble.whatz.application.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            if (favorite.getVideo().equals(videos)) {
                favoriteRepository.delete(favorite);
                return false;
            }
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .video(videos)
                .build();

        favoriteRepository.save(favorite);
        return true;
    }

    public List<FavoritesDto> getFavoriteVideos(Pageable pageable, Principal principal) {
        Member member = getMember(principal);
        Page<Favorite> favorites = favoriteRepository.findByMemberId(member.getId(), pageable);
        List<FavoritesDto> favoritesDtos = favorites.map(favorite ->
                        new FavoritesDto(favorite.getVideo().getId(), favorite.getVideo().getThumbnail().getExecuteFile()))
                .getContent();

        return favoritesDtos;
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
