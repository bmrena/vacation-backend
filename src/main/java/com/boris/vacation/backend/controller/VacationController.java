package com.boris.vacation.backend.controller;

import com.boris.vacation.backend.dto.VacationCreateRequest;
import com.boris.vacation.backend.dto.VacationRowResponse;
import com.boris.vacation.backend.service.VacationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vacations")
@RequiredArgsConstructor
public class VacationController {

    private final VacationService vacationService;

    @GetMapping
    public List<VacationRowResponse> getAllVacations() {
        return vacationService.getAllVacations();
    }

    @GetMapping("/{id}")
    public VacationRowResponse getVacationById(@PathVariable Long id) {
        return vacationService.getVacationById(id);
    }

    @PostMapping
    public VacationRowResponse createVacation(@Valid @RequestBody VacationCreateRequest request) {
        return vacationService.createVacation(request);
    }

    @PatchMapping("/{id}/cancel")
    public VacationRowResponse cancelVacation(@PathVariable Long id) {
        return vacationService.cancelVacation(id);
    }

    @PatchMapping("/{id}/approve-manager")
    public VacationRowResponse approveByManager(
            @PathVariable Long id,
            @RequestParam Long managerId
    ) {
        return vacationService.approveByManager(id, managerId);
    }

    @PatchMapping("/{id}/approve-director")
    public VacationRowResponse approveByDirector(
            @PathVariable Long id,
            @RequestParam Long directorId
    ) {
        return vacationService.approveByDirector(id, directorId);
    }
}
