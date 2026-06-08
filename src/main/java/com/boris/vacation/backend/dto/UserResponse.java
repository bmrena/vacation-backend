package com.boris.vacation.backend.dto;

import com.boris.vacation.backend.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String username;
    private String team;
    private Set<Role> roles;
}
