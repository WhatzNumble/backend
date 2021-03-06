package com.numble.whatz.application.video.service;

import com.numble.whatz.application.category.domain.Category;
import com.numble.whatz.application.category.domain.SubCategory;
import com.numble.whatz.application.category.repository.CategoryRepository;
import com.numble.whatz.application.category.repository.SubCategoryRepository;
import com.numble.whatz.application.like.domain.Favorite;
import com.numble.whatz.application.like.repository.FavoriteRepository;
import com.numble.whatz.application.member.domain.Member;
import com.numble.whatz.application.member.repository.MemberRepository;
import com.numble.whatz.application.thumbnail.domain.Thumbnail;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreDto;
import com.numble.whatz.application.thumbnail.service.ThumbnailStoreExUtil;
import com.numble.whatz.application.video.controller.dto.*;
import com.numble.whatz.application.video.domain.DirectVideo;
import com.numble.whatz.application.video.domain.EmbedVideo;
import com.numble.whatz.application.video.domain.Videos;
import com.numble.whatz.application.video.repository.VideoRepository;
import com.numble.whatz.core.exception.thumbnail.ThumbnailStoreException;
import com.numble.whatz.core.exception.video.VideoStoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoFileService {

    private final VideoRepository videoRepository;
    private final VideoStore videoStore;
    private final MemberRepository memberRepository;
    private final ThumbnailStoreExUtil thumbnailStore;
    private final FavoriteRepository favoriteRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryRepository categoryRepository;

    private static long sequence = 0L;

    @Transactional
    public void saveDirect(DirectDto video, Principal principal) throws VideoStoreException, ThumbnailStoreException {
        Member member = getMember(principal);

        String executeFileName = videoStore.storeVideo(video.getFile());

        ThumbnailStoreDto storeThumbnail = thumbnailStore.storeThumbnail(video.getVideoThumbnail());

        DirectVideo direct = getDirectVideo(video, executeFileName, member, storeThumbnail);

        saveCategory(video.getCategory(), direct);

        videoRepository.save(direct);
    }

    @Transactional
    public void saveEmbed(EmbedDto video, Principal principal) throws ThumbnailStoreException {
        Member member = getMember(principal);

        ThumbnailStoreDto storeThumbnail = thumbnailStore.storeThumbnail(video.getVideoThumbnail());

        EmbedVideo embed = getEmbedVideo(video, member, storeThumbnail);

        saveCategory(video.getCategory(), embed);

        videoRepository.save(embed);
    }

    @Transactional
    public void removeVideo(String id, Principal principal) {
        long parseId = Long.parseLong(id);
        Videos video = getVideos(parseId);
        Member member = getMember(principal);
        checkOwner(video, member);

        if (video instanceof DirectVideo) videoStore.deleteVideo(((DirectVideo)video).getDirectDir());
        thumbnailStore.removeThumbnail(video.getThumbnail());

        List<Favorite> favorites = video.getFavorites();
        for (Favorite favorite : favorites) {
            favoriteRepository.delete(favorite);
        }
        SubCategory subCategory = video.getSubCategory();
        if (subCategory != null) subCategoryRepository.delete(subCategory);
        videoRepository.delete(video);
    }

    @Transactional
    public void modifyDirect(ModifyDirectDto video, Principal principal) throws VideoStoreException, ThumbnailStoreException {
        Member member = getMember(principal);
        Videos videos = getVideos(video.getId());
        checkOwner(videos, member);

        String modifyVideo = videoStore.modifyVideo(video.getFile(), ((DirectVideo) videos).getDirectDir());

        ThumbnailStoreDto thumbnail = thumbnailStore.modifyThumbnail(video.getVideoThumbnail(), videos.getThumbnail());

        ((DirectVideo) videos).modify(modifyVideo, video.getTitle(),
                video.getContent(), thumbnail.getCutName(), thumbnail.getExecuteName());

        modifyCategory(video.getCategory(), videos);
    }

    @Transactional
    public void modifyEmbed(ModifyEmbedDto video, Principal principal) throws ThumbnailStoreException {
        Member member = getMember(principal);
        Videos videos = getVideos(video.getId());
        checkOwner(videos, member);

        ThumbnailStoreDto thumbnail = thumbnailStore.modifyThumbnail(video.getVideoThumbnail(), videos.getThumbnail());

        ((EmbedVideo) videos).modify(video.getLink(), video.getTitle(),
                video.getContent(), thumbnail.getCutName(), thumbnail.getExecuteName());

        modifyCategory(video.getCategory(), videos);
    }

    private void saveCategory(String categoryName, Videos videos) {
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if (!category.isEmpty()) {
            SubCategory subCategory = new SubCategory(category.get(), videos);
            subCategoryRepository.save(subCategory);
            videos.setSubCategory(subCategory);
        }
    }

    private void modifyCategory(String categoryName, Videos videos) {
        SubCategory subCategory = videos.getSubCategory();
        if (subCategory != null) {
            Category category = subCategory.getCategory();
            if (categoryName.isEmpty()) subCategoryRepository.delete(subCategory);
            else if (!category.getName().equals(categoryName)) {
                Optional<Category> findCategory = categoryRepository.findByName(categoryName);
                if (findCategory.isEmpty()) subCategoryRepository.delete(subCategory);
                else subCategory.setCategory(findCategory.get());
            }
        }
    }

    private DirectVideo getDirectVideo(DirectDto video, String executeFileName, Member member, ThumbnailStoreDto storeThumbnail) {
        Thumbnail thumbnail = Thumbnail.builder()
                .cutFile(storeThumbnail.getCutName())
                .executeFile(storeThumbnail.getExecuteName())
                .build();

        DirectVideo direct = DirectVideo.builder()
                .thumbnail(thumbnail)
                .content(video.getContent())
                .title(video.getTitle())
                .member(member)
                .directDir(executeFileName)
                .showId(sequence++)
                .build();

        return direct;
    }

    private EmbedVideo getEmbedVideo(EmbedDto video, Member member, ThumbnailStoreDto storeThumbnail) {
        Thumbnail thumbnail = Thumbnail.builder()
                .cutFile(storeThumbnail.getCutName())
                .executeFile(storeThumbnail.getExecuteName())
                .build();

        EmbedVideo embed = EmbedVideo.builder()
                .content(video.getContent())
                .thumbnail(thumbnail)
                .member(member)
                .title(video.getTitle())
                .link(video.getLink())
                .showId(sequence++)
                .build();
        return embed;
    }

    private Videos getVideos(long parseId) {
        Optional<Videos> findVideo = videoRepository.findById(parseId);
        if (findVideo.isEmpty()) throw new IllegalStateException("?????? ???????????? ???????????? ????????????.");
        Videos video = findVideo.get();
        return video;
    }

    private Member getMember(Principal principal) {
        Optional<Member> findMember = memberRepository.findBySnsId(principal.getName());
        if (findMember.isEmpty()) throw new IllegalStateException("?????? ?????? ???????????? ????????????.");
        Member member = findMember.get();
        return member;
    }

    private void checkOwner(Videos video, Member member) {
        if (!video.getMember().getId().equals(member.getId())) throw new IllegalStateException("????????? ?????? ????????? ????????????.");
    }
}
