package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> get();

    UserDto add(UserDto user);

    UserDto update(Long userId, UserDto user);

    UserDto getById(Long userId);

    void delete(Long userId);
}
