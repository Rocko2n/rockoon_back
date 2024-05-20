package com.rockoon.domain.ticket.dto;

import com.rockoon.domain.ticket.entity.Guest;
import com.rockoon.domain.ticket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {
    private Long id;
    private User user;
    private Guest guest;
    private LocalDateTime issuedAt;
    private boolean used;
}
