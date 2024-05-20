package com.rockoon.domain.ticket.service;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.entity.Guest;
import com.rockoon.domain.ticket.entity.Ticket;
import com.rockoon.domain.ticket.entity.User;
import com.rockoon.domain.ticket.repository.GuestRepository;
import com.rockoon.domain.ticket.repository.TicketRepository;
import com.rockoon.domain.ticket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest) {

        User user = userRepository.findById(ticketRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Guest guest = guestRepository.findById(ticketRequest.getGuestId())
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));

        Ticket ticket = Ticket.builder()
                .user(user)
                .guest(guest)
                .issuedAt(LocalDateTime.now())
                .isUsed(false)
                .build();

        ticket = ticketRepository.save(ticket);


        return new TicketResponse(ticket.getId(), ticket.getUser(), ticket.getGuest(), ticket.getIssuedAt(), ticket.isUsed());
    }



    @Override
    public void useTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setUsed(true);

        ticketRepository.save(ticket);
    }

}
