package com.rockoon.domain.ticket.entity;

import com.rockoon.domain.auditing.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Table(name = "ticket")
public class Ticket extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Transient   /* 난수로 */
    private String data;

    @Column(name = "due_date")
    private Date dueDate;

    @Column(name = "ticket_url", nullable = false)
    private String Url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;

    @PrePersist
    private void generateUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }

    @PrePersist
    private void generateData() {
        /* 난수 생성 로직 추가 */
    }
}
