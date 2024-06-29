package com.rockoon.domain.board.controller;

import com.rockoon.domain.board.converter.PromotionConverter;
import com.rockoon.domain.board.dto.promotion.PromotionRequest;
import com.rockoon.domain.board.dto.promotion.PromotionResponse.PromotionListDto;
import com.rockoon.domain.board.service.promotion.PromotionCommandService;
import com.rockoon.domain.board.service.promotion.PromotionQueryService;
import com.rockoon.domain.member.entity.Member;
import com.rockoon.domain.member.service.MemberQueryService;
import com.rockoon.global.annotation.api.ApiErrorCodeExample;
import com.rockoon.global.annotation.auth.AuthUser;
import com.rockoon.presentation.payload.code.ErrorStatus;
import com.rockoon.presentation.payload.dto.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import static com.rockoon.domain.board.dto.promotion.PromotionResponse.PromotionDetailDto;

@Tag(name = "Promotion API", description = "프로모션 API")
@ApiResponse(responseCode = "2000", description = "성공")
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@RestController
public class PromotionApiController {
    private final PromotionCommandService promotionCommandService;
    private final PromotionQueryService promotionQueryService;
    private final MemberQueryService memberQueryService;

    @Operation(summary = "프로모션 작성 🔑", description = "로그인한 회원이 프로모션(홍보글)을 작성합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND
    })
    @PostMapping
    public ApiResponseDto<Long> createPromotion(@AuthUser Member member,
                                                @RequestBody PromotionRequest promotionRequest) {
        return ApiResponseDto.onSuccess(promotionCommandService.createPromotion(member, promotionRequest));
    }

    @Operation(summary = "프로모션 수정 🔑", description = "로그인한 회원이 프로모션(홍보글)을 작성했던 글을 수정합니다." +
            "권한은 작성자에게만 있습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.PROMOTION_NOT_FOUND,
            ErrorStatus.PROMOTION_ONLY_CAN_BE_TOUCHED_BY_WRITER
    })
    @PutMapping("/{promotionId}")
    public ApiResponseDto<Long> modifyPromotion(@AuthUser Member member,
                                                @PathVariable Long promotionId,
                                                @RequestBody PromotionRequest promotionRequest) {
        return ApiResponseDto.onSuccess(promotionCommandService.modifyPromotion(member, promotionId, promotionRequest));
    }

    @Operation(summary = "프로모션 조회", description = "프로모션의 PK를 통해 글을 조회합니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.PROMOTION_NOT_FOUND
    })
    @GetMapping("/{promotionId}")
    public ApiResponseDto<PromotionDetailDto> getPromotionById(@PathVariable Long promotionId) {
        return ApiResponseDto.onSuccess(
                PromotionConverter.toDetailDto(
                        promotionQueryService.getPromotionById(promotionId)
                )
        );
    }

    @Operation(summary = "프로모션 페이징 조회", description = "프로모션의 리스트를 페이징을 통해 조회합니다." +
            "한페이지당 사이즈는 5개입니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus.PROMOTION_NOT_FOUND
    })
    @GetMapping
    public ApiResponseDto<PromotionListDto> getPromotionList(@RequestParam(defaultValue = "0") int currentPage) {
        Pageable pageable = PageRequest.of(currentPage, 5);
        return ApiResponseDto.onSuccess(
                PromotionConverter.toListDto(
                        promotionQueryService.getPaginationPromotion(pageable)
                )
        );
    }

    @Operation(summary = "프로모션 삭제 🔑", description = "로그인한 회원이 프로모션(홍보글)을 작성했던 글을 삭제합니다." +
            "권한은 작성자에게만 있습니다.")
    @ApiErrorCodeExample({
            ErrorStatus._INTERNAL_SERVER_ERROR,
            ErrorStatus._UNAUTHORIZED_LOGIN_DATA_RETRIEVAL_ERROR,
            ErrorStatus._ASSIGNABLE_PARAMETER,
            ErrorStatus.MEMBER_NOT_FOUND,
            ErrorStatus.PROMOTION_NOT_FOUND,
            ErrorStatus.PROMOTION_ONLY_CAN_BE_TOUCHED_BY_WRITER
    })
    @DeleteMapping("/{promotionId}")
    public ApiResponseDto<Boolean> deletePromotion(@AuthUser Member member,
                                                   @PathVariable Long promotionId) {
        promotionCommandService.removePromotion(member, promotionId);
        return ApiResponseDto.onSuccess(true);
    }
}