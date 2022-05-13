package com.numble.whatz.application.like.controller;

import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import com.numble.whatz.application.like.service.FavoriteService;
import com.numble.whatz.application.video.controller.dto.HomeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("favorite/{id}")
    public boolean favorite(@PathVariable Long id, Principal principal) {
        return favoriteService.toggleFavorite(id, principal);
    }

    @GetMapping("favorite")
    public HomeDto favoriteVideo(@PageableDefault(size = 5) Pageable pageable, Principal principal) {
        HomeDto favoritesDtos = favoriteService.getFavoriteVideos(pageable, principal);
        return favoritesDtos;
    }
}
