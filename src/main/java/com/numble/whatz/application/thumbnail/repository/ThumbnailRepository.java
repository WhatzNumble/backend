package com.numble.whatz.application.thumbnail.repository;

import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
