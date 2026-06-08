package com.boris.vacation.backend.dto;

import com.boris.vacation.backend.enums.DayPart;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VacationCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate firstDay;

    @NotNull
    private DayPart firstDayPart;

    @NotNull
    private LocalDate lastDay;

    @NotNull
    private DayPart lastDayPart;
}