package com.rockoon.domain.ticket.service;

import com.rockoon.domain.ticket.dto.TicketRequest;
import com.rockoon.domain.ticket.dto.TicketResponse;
import com.rockoon.domain.ticket.entity.Guest;
import com.rockoon.domain.ticket.entity.Ticket;
import com.rockoon.domain.ticket.entity.User;
import com.rockoon.domain.ticket.repository.GuestRepository;
import com.rockoon.domain.ticket.repository.TicketRepository;
import com.rockoon.domain.ticket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TicketServiceTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GuestRepository guestRepository;

    private User user;
    private Guest guest;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .password("password")
                .email("testUser@example.com")
                .build();

        guest = Guest.builder()
                .name("testGuest")
                .email("testGuest@example.com")
                .build();

        ticket = Ticket.builder()
                .id(1L)
                .user(user)
                .guest(guest)
                .issuedAt(LocalDateTime.now())
                .isUsed(false)
                .build();
    }

    @Test
    void createTicket() {
        // given
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(guestRepository.findById(any(Long.class))).thenReturn(Optional.of(guest));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setUserId(1L);
        ticketRequest.setGuestId(1L);

        // when
        TicketResponse ticketResponse = ticketService.createTicket(ticketRequest);

        // then
        assertThat(ticketResponse).isNotNull();
        assertThat(ticketResponse.getUser()).isEqualTo(user);
        assertThat(ticketResponse.getGuest()).isEqualTo(guest);
        assertThat(ticketResponse.isUsed()).isFalse();
    }

    @Test
    void useTicket() {
        // given
        when(ticketRepository.findById(any(Long.class))).thenReturn(Optional.of(ticket));

        // when
        ticketService.useTicket(1L);

        // then
        assertThat(ticket.isUsed()).isTrue();
    }
}
