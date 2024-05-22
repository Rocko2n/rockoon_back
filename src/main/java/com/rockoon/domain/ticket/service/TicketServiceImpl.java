package com.rockoon.domain.ticket.service;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.entity.Ticket;
import com.rockoon.domain.ticket.repository.TicketRepository;
import com.rockoon.domain.ticket.entity.Member;
import com.rockoon.domain.ticket.repository.MemberRepository;
import com.rockoon.domain.ticket.entity.Guest;
import com.rockoon.domain.ticket.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final GuestRepository guestRepository;

    @Override
    public TicketResponse createTicket(TicketRequest ticketRequest) {
        Member member = memberRepository.findById(ticketRequest.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Guest guest = guestRepository.findById(ticketRequest.getGuestId())
                .orElseThrow(() -> new IllegalArgumentException("Guest not found"));

        Ticket ticket = Ticket.builder()
                .member(member)
                .guest(guest)
                .createdAt(LocalDateTime.now())
                .used(false)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return new TicketResponse(savedTicket.getId(), savedTicket.getMember().getId(),
                savedTicket.getGuest().getId(), savedTicket.getCreatedAt(),
                savedTicket.isUsed());
    }

    @Override
    public void useTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ticket.setUsed(true);
        ticketRepository.save(ticket);
    }
}
