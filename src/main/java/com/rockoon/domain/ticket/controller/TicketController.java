package com.rockoon.domain.ticket.controller;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return ResponseEntity.ok(ticketResponse);
    }

    @PatchMapping("/{ticketId}/use")
    public ResponseEntity<Void> useTicket(@PathVariable Long ticketId) {
        ticketService.useTicket(ticketId);
        return ResponseEntity.ok().build();
    }
}
