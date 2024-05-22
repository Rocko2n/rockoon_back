package com.rockoon.domain.ticket.service;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;

public interface TicketService {
    TicketResponse createTicket(TicketRequest ticketRequest);
    void useTicket(Long ticketId);
}