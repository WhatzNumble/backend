package com.numble.whatz.application.member.service;

import com.numble.whatz.application.category.domain.SubCategory;
import com.numble.whatz.application.category.repository.SubCategoryRepository;
import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.controller.AdminDto;
import com.numble.whatz.application.member.controller.MemberDto;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.core.oauth.OAuth2Model;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CrudMemberService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public void signUp(OAuth2Model oAuth2Model) {
        memberRepository.save(oAuth2Model.toEntity());

    }

    public boolean isMember(String snsId) {
        Optional<Member> member = memberRepository.findBySnsId(snsId);
        return member.isPresent();
    }

    @Transactional
    public void delete(String snsId) {
        Optional<Member> findMember = memberRepository.findBySnsId(snsId);
        List<SubCategory> categories = findMember.get().getCategories();
        List<Favorite> favorites = findMember.get().getFavorites();
        for (SubCategory category : categories) {
            subCategoryRepository.delete(category);
        }
        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
        }
        memberRepository.deleteBySnsId(snsId);
    }

    public MemberDto getProfile(String snsId) {
        Member member = memberRepository.findBySnsId(snsId)
                .orElseThrow(RuntimeException::new);

        return new MemberDto(member.getEmail(), member.getNickName(), member.getThumbnailUrl());
    }

    public AdminDto getMemberByNickName(AdminDto admin) {
        Member member = memberRepository.findByNickName(admin.getNickName())
                .orElseThrow(() -> new RuntimeException("일치하는 nickname 이 없습니다"));

        if (!passwordEncoder.matches(admin.getPassword(), member.getPassword()))
            throw new RuntimeException("패스워드가 일치하지 않습니다");

        return new AdminDto(member.getNickName(), member.getPassword(), member.getSnsId());
    }
}
