package ru.practicum.shareit.user.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.add(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        userRepository.update(userId, user);
        return getById(userId);
    }

    @Override
    public UserDto getById(Long userId) {
        return UserMapper.toUserDto(userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId))));
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.delete(userId)) {
            throw new NotFoundException(String.format("User with id %d not found", userId));
        }
    }
}
