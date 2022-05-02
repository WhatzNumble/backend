package com.numble.whatz.application.like.service;

import java.security.Principal;

public interface FavoriteService {
    boolean toggleFavorite(Long id, Principal principal);
}
