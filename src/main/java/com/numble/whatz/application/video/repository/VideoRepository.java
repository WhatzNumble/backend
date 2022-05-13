package com.numble.whatz.application.video.repository;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.video.domain.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Videos, Long> {

    @Query(value = "select m from Videos m where m.member = :member")
    Page<Videos> findByMemberWithPageable(@Param("member") Member member, Pageable pageable);

    @Query(value = "select m from Videos m where m.member = :member")
    List<Videos> findByMember(@Param("member") Member member);

    @Query(value = "select m from Videos m where m.showId = :showId")
    Optional<Videos> findByShowId(@Param("showId") String showId);
}
