package com.numble.whatz.application.like.repository;

import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.video.domain.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select m from Favorite m left join m.member")
    Page<Favorite> findByMemberId(Long memberId, Pageable pageable);

    @Query("select f from Favorite f where f.video = :video")
    List<Favorite> findByVideo(@Param("video")Videos video);
}
