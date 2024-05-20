package com.rockoon.domain.ticket.controller;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest ticketRequest) {
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);
        return ResponseEntity.ok(ticketResponse);
    }

    @PostMapping("/{id}/use")
    public ResponseEntity<Void> useTicket(@PathVariable Long id) {
        ticketService.useTicket(id);
        return ResponseEntity.ok().build();
    }
}
