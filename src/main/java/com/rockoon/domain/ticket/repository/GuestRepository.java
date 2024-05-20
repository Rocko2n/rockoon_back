package com.rockoon.domain.ticket.repository;

import com.rockoon.domain.ticket.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
