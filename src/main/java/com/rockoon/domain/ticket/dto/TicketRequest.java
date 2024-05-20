package com.rockoon.domain.ticket.dto;

import lombok.Data;

@Data
public class TicketRequest {

    private Long userId;

    private Long guestId;
}
