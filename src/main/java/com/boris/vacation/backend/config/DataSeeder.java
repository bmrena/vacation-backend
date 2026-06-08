package com.boris.vacation.backend.config;

import com.boris.vacation.backend.entity.AppUser;
import com.boris.vacation.backend.entity.Vacation;
import com.boris.vacation.backend.enums.DayPart;
import com.boris.vacation.backend.enums.Role;
import com.boris.vacation.backend.enums.VacationStatus;
import com.boris.vacation.backend.repository.AppUserRepository;
import com.boris.vacation.backend.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final VacationRepository vacationRepository;

    @Override
    public void run(String... args) {
        if (appUserRepository.count() > 0) {
            return;
        }

        AppUser michal = new AppUser();
        michal.setFullName("Michal Makas");
        michal.setUsername("mmakas");
        michal.setTeam("Development");
        michal.setRoles(Set.of(Role.USER, Role.MANAGER));
        appUserRepository.save(michal);

        AppUser martin = new AppUser();
        martin.setFullName("Martin");
        martin.setUsername("martin");
        martin.setTeam("Development");
        martin.setRoles(Set.of(Role.MANAGER));
        appUserRepository.save(martin);

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2016, 8, 14, 9, 0),
                LocalDate.of(2016, 9, 5),
                DayPart.FULL_DAY,
                LocalDate.of(2016, 9, 8),
                DayPart.FULL_DAY,
                BigDecimal.valueOf(4.0)
        );

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2016, 12, 5, 9, 0),
                LocalDate.of(2016, 12, 7),
                DayPart.FULL_DAY,
                LocalDate.of(2016, 12, 7),
                DayPart.FULL_DAY,
                BigDecimal.valueOf(1.0)
        );

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2016, 10, 31, 9, 0),
                LocalDate.of(2016, 12, 22),
                DayPart.FULL_DAY,
                LocalDate.of(2016, 12, 23),
                DayPart.FULL_DAY,
                BigDecimal.valueOf(2.0)
        );

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2016, 10, 31, 9, 5),
                LocalDate.of(2016, 12, 27),
                DayPart.FULL_DAY,
                LocalDate.of(2016, 12, 30),
                DayPart.FULL_DAY,
                BigDecimal.valueOf(4.0)
        );

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2016, 10, 31, 9, 10),
                LocalDate.of(2017, 1, 2),
                DayPart.FULL_DAY,
                LocalDate.of(2017, 1, 3),
                DayPart.FULL_DAY,
                BigDecimal.valueOf(2.0)
        );

        createVacationRequest(
                michal,
                martin,
                LocalDateTime.of(2017, 2, 7, 9, 0),
                LocalDate.of(2017, 2, 24),
                DayPart.EVENING,
                LocalDate.of(2017, 2, 25),
                DayPart.MORNING,
                BigDecimal.valueOf(0.5)
        );
    }

    private void createVacationRequest(
            AppUser user,
            AppUser manager,
            LocalDateTime createdAt,
            LocalDate firstDay,
            DayPart firstDayPart,
            LocalDate lastDay,
            DayPart lastDayPart,
            BigDecimal days
    ) {
        Vacation request = new Vacation();
        request.setUser(user);
        request.setApprovedByManager(manager);
        request.setApprovedByDirector(null);
        request.setCreatedAt(createdAt);
        request.setStatus(VacationStatus.APPROVED_BY_MANAGER);
        request.setFirstDay(firstDay);
        request.setFirstDayPart(firstDayPart);
        request.setLastDay(lastDay);
        request.setLastDayPart(lastDayPart);
        request.setDays(days);

        vacationRepository.save(request);
    }
}