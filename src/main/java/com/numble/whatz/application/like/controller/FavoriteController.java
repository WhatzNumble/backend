package com.numble.whatz.application.like.controller;

import com.numble.whatz.application.like.service.FavoriteService;
import com.numble.whatz.application.video.controller.dto.FavoritesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("api/favorite/{id}")
    public boolean favorite(@PathVariable Long id, Principal principal) {
        return favoriteService.toggleFavorite(id, principal);
    }

    @GetMapping("api/favorite")
    public List<FavoritesDto> favoriteVideo(@PageableDefault(size = 5) Pageable pageable) {
        // ======== 서비스가 생기면 여기는 지우는 부분 ========
        FavoritesDto favoritesDto1 = new FavoritesDto(1L, "videoThumbnail1", 30L);
        FavoritesDto favoritesDto2 = new FavoritesDto(1L, "videoThumbnail1", 30L);

        List<FavoritesDto> favoritesDtos = new ArrayList<>();
        favoritesDtos.add(favoritesDto1);
        favoritesDtos.add(favoritesDto2);
        // ======== 서비스가 생기면 여기는 지우는 부분 ========

        return favoritesDtos;
    }
}
