package com.boris.vacation.backend.service;

import com.boris.vacation.backend.dto.UserResponse;
import com.boris.vacation.backend.dto.VacationRowResponse;
import com.boris.vacation.backend.dto.VacationScreenResponse;
import com.boris.vacation.backend.entity.AppUser;
import com.boris.vacation.backend.entity.Vacation;
import com.boris.vacation.backend.enums.VacationStatus;
import com.boris.vacation.backend.mapper.UserMapper;
import com.boris.vacation.backend.mapper.VacationMapper;
import com.boris.vacation.backend.repository.AppUserRepository;
import com.boris.vacation.backend.repository.VacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationScreenService {

    private final AppUserRepository appUserRepository;
    private final VacationRepository vacationRepository;
    private final UserMapper userMapper;
    private final VacationMapper vacationMapper;

    public VacationScreenResponse getScreenData(Long userId) {
        AppUser selectedUser = appUserRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        List<AppUser> users = appUserRepository.findAll();

        List<Vacation> vacations = vacationRepository.findByUserIdOrderByCreatedAtDesc(userId);

        VacationScreenResponse response = new VacationScreenResponse();
        response.setTeams(mapTeams(users));
        response.setUsers(users.stream().map(userMapper::toUserResponse).toList());
        response.setSelectedUser(userMapper.toUserResponse(selectedUser));
        response.setVacations(vacations.stream().map(vacationMapper::toVacationRowResponse).toList());

        return response;
    }

    private List<String> mapTeams(List<AppUser> users) {
        List<String> teams = new ArrayList<>();
        teams.add("Všetci používateľia");

        List<String> userTeams = users.stream()
                .map(AppUser::getTeam)
                .filter(team -> team != null && !team.isBlank())
                .distinct()
                .toList();

        teams.addAll(userTeams);

        return teams;
    }
}