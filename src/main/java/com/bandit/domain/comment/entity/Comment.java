package com.bandit.domain.comment.entity;

import com.bandit.domain.auditing.entity.BaseTimeEntity;
import com.bandit.domain.board.entity.Promotion;
import com.bandit.domain.comment.dto.CommentRequest;
import com.bandit.domain.member.entity.Member;
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
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    public static Comment of(Member member, Promotion promotion, CommentRequest request) {
        return Comment.builder()
                .content(request.getContent())
                .promotion(promotion)
                .writer(member)
                .build();
    }
}
