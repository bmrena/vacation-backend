package com.boris.vacation.backend.entity;

import com.boris.vacation.backend.enums.DayPart;
import com.boris.vacation.backend.enums.VacationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vacation")
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationStatus status;

    @Column(nullable = false)
    private LocalDate firstDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayPart firstDayPart;

    @Column(nullable = false)
    private LocalDate lastDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayPart lastDayPart;

    @Column(nullable = false)
    private BigDecimal days;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name = "approved_by_manager_id")
    private AppUser approvedByManager;

    @ManyToOne
    @JoinColumn(name = "approved_by_director_id")
    private AppUser approvedByDirector;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
