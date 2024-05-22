package com.rockoon.domain.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private Long memberId;
    private Long guestId;
    private LocalDateTime createdAt;
    private boolean used;
}
