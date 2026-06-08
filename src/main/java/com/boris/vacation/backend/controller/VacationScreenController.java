package com.boris.vacation.backend.controller;

import com.boris.vacation.backend.dto.VacationScreenResponse;
import com.boris.vacation.backend.service.VacationScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vacation-screen")
@RequiredArgsConstructor
public class VacationScreenController {

    private final VacationScreenService vacationScreenService;

    @GetMapping
    public VacationScreenResponse getScreenData(@RequestParam Long userId){
        return vacationScreenService.getScreenData(userId);
    }
}
