package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

public interface UserService {
    UserDtoResponse add(UserDtoRequestCreate userDtoRequestCreate);

    UserDtoResponse update(Long userId, UserDtoRequestUpdate userDtoRequestUpdate);

    UserDtoResponse getById(Long userId);

    void delete(Long userId);
}
