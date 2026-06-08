package com.boris.vacation.backend.dto;

import com.boris.vacation.backend.enums.DayPart;
import com.boris.vacation.backend.enums.VacationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VacationRowResponse {

    private Long id;
    private LocalDate createdDate;
    private VacationStatus status;
    private LocalDate firstDay;
    private DayPart firstDayPart;
    private LocalDate lastDay;
    private DayPart lastDayPart;
    private BigDecimal days;
    private String approvedByManager;
    private String approvedByDirector;
    private boolean canCancel;
}