package com.numble.whatz.application.like.service;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.service.CrudMemberService;
import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.member.service.MemberService;
import com.numble.whatz.application.video.domain.Videos;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final VideoService videoService;
    private final MemberService memberService;

    @Override
    @Transactional
    public boolean toggleFavorite(Long id, Principal principal) {
        Member member = memberService.getMemberBySnsId(principal.getName());
        Videos findVideo = videoService.getVideoById(id);
        List<Favorite> favorites = member.getFavorites();
        for (Favorite favorite : favorites) {
            if (favorite.getVideo().equals(findVideo)) {
                favoriteRepository.delete(favorite);
                return false;
            }
        }

        Favorite favorite = Favorite.builder()
                .member(member)
                .video(findVideo)
                .build();

        favoriteRepository.save(favorite);
        return true;
    }

    @Override
    public List<FavoritesDto> getFavoriteVideos(Pageable pageable, Principal principal) {
        List<FavoritesDto> favoritesDtos = new ArrayList<>();
        Member findMember = memberService.getMemberBySnsId(principal.getName());
        Page<Favorite> favorites = favoriteRepository.findByMemberId(findMember.getId(), pageable);
        for (Favorite favorite : favorites) {
            Videos video = favorite.getVideo();
            FavoritesDto favoritesDto = new FavoritesDto(video.getId(), video.getThumbnail().getExecuteFile());
            favoritesDtos.add(favoritesDto);
        }

        return favoritesDtos;
    }
}
