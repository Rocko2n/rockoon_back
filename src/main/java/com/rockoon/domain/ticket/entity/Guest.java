package com.rockoon.domain.ticket.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rockoon.domain.auditing.entity.BaseTimeEntity;
import com.rockoon.domain.board.entity.Promotion;
import com.rockoon.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Table(name = "guest")
public class Guest extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;


    private String name;

    @Column(name = "isTicketIssued")
    @JsonProperty("ticketIssued")
    private Boolean ticketIssued;

    @Column(name = "isEntered")
    @JsonProperty("entered")
    private Boolean entered;

    public void setTicketIssued(Boolean ticketIssued) {
        this.ticketIssued = ticketIssued;
    }

    public void setEntered(Boolean entered) {
        this.entered = entered;
    }

    public void setName(String name) {
        this.name = name;
    }
}