package com.rockoon.domain.board.promotion.service;

import com.rockoon.domain.board.promotion.dto.PromotionRequest;
import com.rockoon.domain.board.promotion.entity.Promotion;
import com.rockoon.domain.board.promotion.repository.PromotionRepository;
import com.rockoon.domain.image.dto.ImageRequest;
import com.rockoon.domain.image.entity.Image;
import com.rockoon.domain.image.repository.ImageRepository;
import com.rockoon.domain.member.entity.Member;
import com.rockoon.domain.member.entity.Role;
import com.rockoon.domain.member.repository.MemberRepository;
import com.rockoon.domain.music.dto.MusicRequest;
import com.rockoon.domain.music.entity.Music;
import com.rockoon.domain.music.repository.MusicRepository;
import com.rockoon.domain.option.dto.OptionRequest;
import com.rockoon.domain.option.entity.Category;
import com.rockoon.domain.option.entity.Option;
import com.rockoon.domain.option.repository.OptionRepository;
import com.rockoon.domain.team.service.TeamCommandService;
import com.rockoon.global.test.DatabaseCleanUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
class PromotionCommandServiceTest {
    //command Service
    @Autowired
    PromotionCommandService promotionCommandService;
    @Autowired
    TeamCommandService teamCommandService;
    //repository
    @Autowired
    PromotionRepository promotionRepository;
    @Autowired
    OptionRepository optionRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    MusicRepository musicRepository;
    @Autowired
    MemberRepository memberRepository;
    //else
    @Autowired
    DatabaseCleanUp databaseCleanUp;

    //entity & requestDto
    Member member1;
    Member member2;
    Long teamId;
    List<OptionRequest> optionList = new ArrayList<>();

    List<ImageRequest> imageList = new ArrayList<>();
    List<MusicRequest> musicList = new ArrayList<>();

    PromotionRequest request;
    @BeforeEach
    void setUp() {
        member1 = Member.builder()
                .role(Role.USER)
                .name("이정한")
                .profileImg("img")
                .kakaoEmail("kakao@naver.com")
                .username("hann")
                .nickname("hann")
                .build();
        member2 = Member.builder()
                .role(Role.USER)
                .name("이정한")
                .profileImg("img")
                .kakaoEmail("kakao1@naver.com")
                .username("Hann")
                .nickname("Hann")
                .build();

        optionList.add(OptionRequest.builder()
                .category(Category.TIME)
                .content("content")
                .build());
        optionList.add(OptionRequest.builder()
                .category(Category.FEE)
                .content("content")
                .build());
        imageList.add(ImageRequest.builder()
                .imageUrl("image.png")
                .build());
        musicList.add(MusicRequest.builder()
                .artist("thornApple")
                .title("blueSpring")
                .build());
        musicList.add(MusicRequest.builder()
                .artist("silicagel")
                .title("tiktaktok")
                .build());

        memberRepository.save(member1);
        memberRepository.save(member2);

    }

    @AfterEach
    void cleanUp() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    @DisplayName("유저가 promotion글을 작성한다. 연관 엔티티 작성 제외")
    void createPromotionWithoutRelationEntity() {
        //given
        request = PromotionRequest.builder()
                .title("promotion test")
                .content("promotion")
                .maxAudience(3)
                .build();
        //when
        Long promotionId = promotionCommandService.savePromotion(member1, request);

        //then
        Promotion promotion = promotionRepository.findById(promotionId).get();
        assertThat(promotion.getMaxAudience()).isEqualTo(request.getMaxAudience());
        assertThat(promotion.getContent()).isEqualTo(request.getContent());
        assertThat(promotion.getTitle()).isEqualTo(request.getTitle());
    }

    @Test
    @DisplayName("유저가 promotion글을 작성한다. 연관 엔티티 포함(image, option, music)")
    void createPromotionWithRelationEntity() {
        //given
        request = PromotionRequest.builder()
                .title("promotion test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();

        //when
        Long promotionId = promotionCommandService.savePromotion(member1, request);

        //then
        List<Option> optionsByBoardId = optionRepository.findOptionsByBoardId(promotionId);
        List<Image> imagesByBoardId = imageRepository.findImagesByBoardId(promotionId);
        List<Music> musicsByBoardId = musicRepository.findMusicsByPromotionId(promotionId);
        assertThat(optionsByBoardId).hasSize(2);
        assertThat(imagesByBoardId).hasSize(1);
        assertThat(musicsByBoardId).hasSize(2);

    }

    @Test
    @DisplayName("등록된 Promotion을 연관 데이터와 함께 수정합니다.")
    void updatePromotion() {
        //given
        request = PromotionRequest.builder()
                .title("promotion test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();
        Long promotionId = promotionCommandService.savePromotion(member1, request);
        imageList.remove(0);
        optionList.add(OptionRequest.builder().build());
        PromotionRequest updateRequest = PromotionRequest.builder()
                .title("promotion update test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();

        //when
        Long updatePromotionId = promotionCommandService.modifyPromotion(member1, promotionId, updateRequest);
        //then
        Promotion promotion = promotionRepository.findById(updatePromotionId).get();
        assertThat(promotion.getTitle()).isEqualTo(updateRequest.getTitle());
        List<Option> optionsByBoardId = optionRepository.findOptionsByBoardId(updatePromotionId);
        List<Image> imagesByBoardId = imageRepository.findImagesByBoardId(updatePromotionId);
        List<Music> musicsByBoardId = musicRepository.findMusicsByPromotionId(updatePromotionId);
        assertThat(optionsByBoardId).hasSize(3);
        assertThat(imagesByBoardId).hasSize(0);
        assertThat(musicsByBoardId).hasSize(2);
    }

    @Test
    @DisplayName("등록되지 않은 Promotion을 수정 할 때, board를 찾는 데 실패하는 예외를 확인합니다.")
    void executeExceptionWhenUpdatePromotion() {
        //given
        PromotionRequest updateRequest = PromotionRequest.builder()
                .title("promotion update test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();

        //when & then
        assertThatThrownBy(() -> promotionCommandService.modifyPromotion(member1, 0L, updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("not found promotion");

    }

    @Test
    @DisplayName("등록된 promotion을 지우고 db의 결과를 확인합니다.")
    void deletePromotion() {
        //given
        PromotionRequest createRequest = PromotionRequest.builder()
                .title("promotion update test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();
        Long promotionId = promotionCommandService.savePromotion(member1, createRequest);
        //when
        log.info("db = {}", memberRepository.findById(member1.getId()).get());
        promotionCommandService.removePromotion(member1, promotionId);
        //then
        List<Promotion> all = promotionRepository.findAll();
        assertThat(all).hasSize(0);
    }
    @Test
    @DisplayName("등록된 promotion을 지울 때, 작성자가 아닐 경우에 예외를 확인합니다.")
    void executeExceptionWhenRemovePromotion() {
        //given
        PromotionRequest createRequest = PromotionRequest.builder()
                .title("promotion update test")
                .content("promotion")
                .imageList(imageList)
                .optionList(optionList)
                .musicList(musicList)
                .maxAudience(3)
                .build();
        Long promotionId = promotionCommandService.savePromotion(member1, createRequest);
        //when & then
        assertThatThrownBy(() -> promotionCommandService.removePromotion(member2, promotionId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("cannot touch it");
    }
}