package ru.practicum.shareit.user.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exemption.DuplicationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoCreateRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdateRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDtoResponse add(UserDtoCreateRequest userDtoCreateRequest) {
        try {
            User user = userMapper.toUserCreate(userDtoCreateRequest);
            return userMapper.toUserDtoResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException e){
            if (e.getMessage().contains("users_email_key")) {
                throw new DuplicationException(String.format("User with email %s already exists", userDtoCreateRequest.getEmail()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public UserDtoResponse update(Long userId, UserDtoUpdateRequest userDtoUpdateRequest) {
        User user = userMapper.toUserUpdate(userDtoUpdateRequest);
        userRepository.update(userId, user);
        return getById(userId);
    }

    @Override
    public UserDtoResponse getById(Long userId) {
        return userMapper.toUserDtoResponse(userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId))));
    }

    @Override
    public void delete(Long userId) {
        userRepository.delete(userId);
    }
}
