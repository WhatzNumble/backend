package com.numble.whatz.application.like.service;

import com.numble.whatz.application.like.controller.dto.FavoritesDto;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface FavoriteService {
    boolean toggleFavorite(Long id, Principal principal);
    List<FavoritesDto> getFavoriteVideos(Pageable pageable, Principal principal);
}
