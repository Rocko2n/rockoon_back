package com.rockoon.domain.ticket.service;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.entity.Ticket;
import com.rockoon.domain.ticket.repository.TicketRepository;
import com.rockoon.domain.ticket.entity.Member;
import com.rockoon.domain.ticket.repository.MemberRepository;
import com.rockoon.domain.ticket.entity.Guest;
import com.rockoon.domain.ticket.repository.GuestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GuestRepository guestRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Member member;
    private Guest guest;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .email("test@example.com")
                .build();

        guest = Guest.builder()
                .id(1L)
                .member(member)
                .guestInfo("Guest info")
                .build();

        ticket = Ticket.builder()
                .id(1L)
                .member(member)
                .guest(guest)
                .createdAt(LocalDateTime.now())
                .used(false)
                .build();
    }

    @Test
    void createTicket_success() {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.of(guest));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketRequest request = new TicketRequest(member.getId(), guest.getId());
        TicketResponse response = ticketService.createTicket(request);

        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void createTicket_memberNotFound() {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        TicketRequest request = new TicketRequest(member.getId(), guest.getId());

        assertThrows(IllegalArgumentException.class, () -> ticketService.createTicket(request));
    }

    @Test
    void createTicket_guestNotFound() {
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        TicketRequest request = new TicketRequest(member.getId(), guest.getId());

        assertThrows(IllegalArgumentException.class, () -> ticketService.createTicket(request));
    }

    @Test
    void useTicket_success() {
        when(ticketRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

        ticketService.useTicket(ticket.getId());

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void useTicket_ticketNotFound() {
        when(ticketRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ticketService.useTicket(ticket.getId()));
    }
}
