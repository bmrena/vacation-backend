package com.boris.vacation.backend.service;

import com.boris.vacation.backend.dto.UserResponse;
import com.boris.vacation.backend.entity.AppUser;
import com.boris.vacation.backend.mapper.UserMapper;
import com.boris.vacation.backend.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        return appUserRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        AppUser user = appUserRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return userMapper.toUserResponse(user);
    }
}