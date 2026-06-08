package com.boris.vacation.backend.mapper;

import com.boris.vacation.backend.dto.UserResponse;
import com.boris.vacation.backend.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(AppUser user) {
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setUsername(user.getUsername());
        response.setTeam(user.getTeam());
        response.setRoles(user.getRoles());

        return response;
    }
}