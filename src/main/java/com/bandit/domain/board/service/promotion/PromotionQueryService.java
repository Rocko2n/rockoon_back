package com.bandit.domain.board.service.promotion;

import com.bandit.domain.board.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PromotionQueryService {
    List<Promotion> getAll();

    List<Promotion> getAllByLatest();

    Page<Promotion> getPaginationPromotion(Pageable pageable);

    Page<Promotion> searchPaginationPromotion(String keyword, Pageable pageable);
    Promotion getPromotionById(Long promotionId);
}
