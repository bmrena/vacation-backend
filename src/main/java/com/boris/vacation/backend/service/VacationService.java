package com.boris.vacation.backend.service;

import com.boris.vacation.backend.dto.UserResponse;
import com.boris.vacation.backend.dto.VacationCreateRequest;
import com.boris.vacation.backend.dto.VacationRowResponse;
import com.boris.vacation.backend.entity.AppUser;
import com.boris.vacation.backend.entity.Vacation;
import com.boris.vacation.backend.enums.DayPart;
import com.boris.vacation.backend.enums.Role;
import com.boris.vacation.backend.enums.VacationStatus;
import com.boris.vacation.backend.mapper.VacationMapper;
import com.boris.vacation.backend.repository.AppUserRepository;
import com.boris.vacation.backend.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final AppUserRepository appUserRepository;
    private final VacationRepository vacationRepository;
    private final VacationMapper vacationMapper;

    public List<VacationRowResponse> getAllVacations() {
        return vacationRepository.findAll()
                .stream()
                .map(vacationMapper::toVacationRowResponse)
                .toList();
    }

    public VacationRowResponse getVacationById(Long id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vacation not found"
                ));

        return vacationMapper.toVacationRowResponse(vacation);
    }

    public VacationRowResponse createVacation(VacationCreateRequest request) {
        AppUser user = appUserRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        if (request.getLastDay().isBefore(request.getFirstDay())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Last day cannot be before first day"
            );
        }

        validateDayPartCombination(
                request.getFirstDay(),
                request.getFirstDayPart(),
                request.getLastDay(),
                request.getLastDayPart()
        );

        BigDecimal calculatedDays = calculateVacationDays(
                request.getFirstDay(),
                request.getFirstDayPart(),
                request.getLastDay(),
                request.getLastDayPart()
        );

        if (calculatedDays.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vacation must contain at least half day"
            );
        }

        Vacation vacation = new Vacation();
        vacation.setUser(user);
        vacation.setStatus(VacationStatus.PENDING);
        vacation.setFirstDay(request.getFirstDay());
        vacation.setFirstDayPart(request.getFirstDayPart());
        vacation.setLastDay(request.getLastDay());
        vacation.setLastDayPart(request.getLastDayPart());
        vacation.setDays(calculatedDays);
        vacation.setApprovedByManager(null);
        vacation.setApprovedByDirector(null);

        Vacation savedVacation = vacationRepository.save(vacation);

        return vacationMapper.toVacationRowResponse(savedVacation);
    }

    public VacationRowResponse cancelVacation(Long id) {
        Vacation vacation = vacationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vacation not found"
                ));

        if (vacation.getStatus() == VacationStatus.CANCELED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vacation is already canceled"
            );
        }

        if (vacation.getStatus() == VacationStatus.REJECTED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rejected vacation cannot be canceled"
            );
        }

        vacation.setStatus(VacationStatus.CANCELED);

        Vacation savedVacation = vacationRepository.save(vacation);

        return vacationMapper.toVacationRowResponse(savedVacation);
    }

    public VacationRowResponse approveByManager(Long vacationId, Long managerId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vacation not found"
                ));

        AppUser manager = appUserRepository.findById(managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Manager not found"
                ));

        if (!manager.getRoles().contains(Role.MANAGER)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a manager"
            );
        }

        if (vacation.getStatus() != VacationStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending vacation can be approved by manager"
            );
        }

        vacation.setApprovedByManager(manager);
        vacation.setStatus(VacationStatus.APPROVED_BY_MANAGER);

        Vacation savedVacation = vacationRepository.save(vacation);

        return vacationMapper.toVacationRowResponse(savedVacation);
    }

    public VacationRowResponse approveByDirector(Long vacationId, Long directorId) {
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vacation not found"
                ));

        AppUser director = appUserRepository.findById(directorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Director not found"
                ));

        if (!director.getRoles().contains(Role.DIRECTOR)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected user is not a director"
            );
        }

        if (vacation.getStatus() != VacationStatus.APPROVED_BY_MANAGER) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Vacation must be approved by manager first"
            );
        }

        vacation.setApprovedByDirector(director);
        vacation.setStatus(VacationStatus.APPROVED_BY_DIRECTOR);

        Vacation savedVacation = vacationRepository.save(vacation);

        return vacationMapper.toVacationRowResponse(savedVacation);
    }

    private BigDecimal calculateVacationDays(
            LocalDate firstDay,
            DayPart firstDayPart,
            LocalDate lastDay,
            DayPart lastDayPart
    ) {
        long calendarDays = ChronoUnit.DAYS.between(firstDay, lastDay) + 1;

        BigDecimal days = BigDecimal.valueOf(calendarDays);

        if (firstDayPart == DayPart.EVENING) {
            days = days.subtract(BigDecimal.valueOf(0.5));
        }

        if (lastDayPart == DayPart.MORNING) {
            days = days.subtract(BigDecimal.valueOf(0.5));
        }

        return days;
    }

    private void validateDayPartCombination(
            LocalDate firstDay,
            DayPart firstDayPart,
            LocalDate lastDay,
            DayPart lastDayPart
    ) {
        if (firstDay.equals(lastDay)
                && firstDayPart == DayPart.EVENING
                && lastDayPart == DayPart.MORNING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid day part combination"
            );
        }
    }
}