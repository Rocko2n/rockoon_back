package com.bandit.domain.showOption.entity;

import com.bandit.domain.board.entity.Board;
import com.bandit.domain.auditing.entity.BaseTimeEntity;
import com.bandit.domain.showOption.dto.ShowOptionRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Table(name = "show_option")
public class ShowOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_option_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static ShowOption of(Board board, ShowOptionRequest request) {
        return ShowOption.builder()
                .category(request.getCategory())
                .content(request.getContent())
                .board(board)
                .build();
    }
}