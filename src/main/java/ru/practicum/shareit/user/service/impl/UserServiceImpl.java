package ru.practicum.shareit.user.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exemption.DuplicationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDtoResponse add(UserDtoRequestCreate userDtoRequestCreate) {
        try {
            User user = userMapper.toUserCreate(userDtoRequestCreate);
            return userMapper.toUserDtoResponse(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("users_email_key")) {
                throw new DuplicationException(String.format("User with email %s already exists", userDtoRequestCreate.getEmail()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public UserDtoResponse update(Long userId, UserDtoRequestUpdate userDtoRequestUpdate) {
        try {
            User userFromBd = userMapper.toUser(getById(userId));
            if (userDtoRequestUpdate.getEmail() != null) {
                userFromBd.setEmail(userDtoRequestUpdate.getEmail());
            }
            if (userDtoRequestUpdate.getName() != null) {
                userFromBd.setName(userDtoRequestUpdate.getName());
            }
            return userMapper.toUserDtoResponse(userRepository.save(userFromBd));
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("users_email_key")) {
                throw new DuplicationException(String.format("User with afsdfasdfasdfasdfasd %s already exists", userDtoRequestUpdate.getEmail()));
            } else {
                throw e;
            }
        }
    }

    @Override
    public UserDtoResponse getById(Long userId) {
        return userMapper.toUserDtoResponse(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId))));
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}