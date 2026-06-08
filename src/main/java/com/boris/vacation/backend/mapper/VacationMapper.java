package com.boris.vacation.backend.mapper;

import com.boris.vacation.backend.dto.VacationRowResponse;
import com.boris.vacation.backend.entity.Vacation;
import com.boris.vacation.backend.enums.VacationStatus;
import org.springframework.stereotype.Component;

@Component
public class VacationMapper {

    public VacationRowResponse toVacationRowResponse(Vacation vacation) {
        VacationRowResponse response = new VacationRowResponse();

        response.setId(vacation.getId());
        response.setCreatedDate(vacation.getCreatedAt().toLocalDate());
        response.setStatus(vacation.getStatus());
        response.setFirstDay(vacation.getFirstDay());
        response.setFirstDayPart(vacation.getFirstDayPart());
        response.setLastDay(vacation.getLastDay());
        response.setLastDayPart(vacation.getLastDayPart());
        response.setDays(vacation.getDays());

        if (vacation.getApprovedByManager() != null) {
            response.setApprovedByManager(vacation.getApprovedByManager().getFullName());
        }

        if (vacation.getApprovedByDirector() != null) {
            response.setApprovedByDirector(vacation.getApprovedByDirector().getFullName());
        }

        response.setCanCancel(canCancel(vacation));

        return response;
    }

    private boolean canCancel(Vacation vacation) {
        return vacation.getStatus() != VacationStatus.CANCELED
                && vacation.getStatus() != VacationStatus.REJECTED;
    }
}