package com.bandit.domain.board.repository;

import com.bandit.domain.board.entity.Promotion;
import com.bandit.domain.board.repository.querydsl.PromotionQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, PromotionQueryRepository {
    Page<Promotion> findAll(Pageable pageable);

    List<Promotion> findPromotionsByOrderByCreatedDateAsc();
}
