package com.boris.vacation.backend.repository;

import com.boris.vacation.backend.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
    List<Vacation> findByUserIdOrderByCreatedAtDesc(Long userId);
}
