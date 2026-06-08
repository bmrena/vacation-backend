package com.boris.vacation.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class VacationScreenResponse {

    private List<String> teams;
    private List<UserResponse> users;
    private UserResponse selectedUser;
    private List<VacationRowResponse> vacations;
}
