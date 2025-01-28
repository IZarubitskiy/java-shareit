package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDtoCreateRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdateRequest;

public interface UserService {
    UserDtoResponse add(UserDtoCreateRequest userDtoCreateRequest);

    UserDtoResponse update(Long userId, UserDtoUpdateRequest userDtoUpdateRequest);

    UserDtoResponse getById(Long userId);

    void delete(Long userId);
}
