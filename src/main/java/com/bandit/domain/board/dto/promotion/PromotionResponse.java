package com.bandit.domain.board.dto.promotion;

import com.bandit.domain.member.dto.MemberResponse;
import com.bandit.domain.music.dto.MusicResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PromotionResponse {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionDetailDto{
        private Long promotionId;
        private String title;
        private String content;
        private String team;
        private int entranceFee;
        private int maxAudience;
        private LocalDate date;
        private String startTime;
        private String endTime;
        private String location;
        private String bankName;
        private String account;
        private String accountHolder;
        private String refundInfo;
        private MemberResponse writer;

        private List<MusicResponse> musicList;
        private List<String> imageList;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionSummaryDto{
        private Long promotionId;
        private String title;
        private String team;
        private String thumbnail;
        private LocalDate date;
        private String location;
        private String startTime;
        private String endTime;
        private int entranceFee;
        private MemberResponse writer;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PromotionListDto{
        private List<PromotionSummaryDto> promotionList;
    }
}
