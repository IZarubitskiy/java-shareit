package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto add(UserDto user);

    UserDto update(Long userId, UserDto user);

    UserDto getById(Long userId);

    void delete(Long userId);
}
