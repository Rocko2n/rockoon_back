package com.rockoon.domain.ticket.repository;

import com.rockoon.domain.ticket.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
