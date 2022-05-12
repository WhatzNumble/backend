package com.numble.whatz.application.video.service;

import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.video.controller.MainContentsDto;
import com.numble.whatz.application.video.controller.dto.MainContentDto;
import com.numble.whatz.application.video.controller.dto.UserVideoDto;
import com.numble.whatz.application.video.controller.dto.UserVideosDto;
import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    public UserVideosDto getUserVideos(Long id) {
        Member member = getMember(id);
        List<Videos> findMember = videoRepository.findByMember(member);

        /*
        List<UserVideoDto> userVideoDtos = findVideos.map(videos ->
                new UserVideoDto(videos.getId(),
                        videos.getVideoTitle(),
                        videos.getVideoContent(),
                        videos.getThumbnail().getCutFile())).toList();

         */

        List<UserVideoDto> userVideoDtos = findMember.stream().map(videos ->
                new UserVideoDto(videos.getId(),
                        videos.getVideoTitle(),
                        videos.getVideoContent(),
                        videos.getThumbnail().getCutFile())).collect(Collectors.toList());

        return new UserVideosDto(member.getId(), member.getEmail(), member.getNickName(), userVideoDtos);
    }

    private Member getMember(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if (findMember.isEmpty()) throw new IllegalStateException("해당 회원 존재하지 않습니다.");
        Member member = findMember.get();
        return member;
    }

    public MainContentsDto getMainContent(Pageable pageable) {
        Page<Videos> page = videoRepository.findAll(pageable);
        List<MainContentDto> list = page.map(videos ->
                new MainContentDto(videos.getId(),
                        videos.getThumbnail().getCutFile(),
                        videos.getVideoTitle(), videos.getMember().getNickName())).toList();
        
    }
}
