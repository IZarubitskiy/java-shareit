package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> get();

    User add(User user);

    User update(Long userId, User user);

    Optional<User> getById(Long userId);

    boolean delete(Long userId);
}
