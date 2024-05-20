package com.rockoon.domain.ticket.repository;

import com.rockoon.domain.ticket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
