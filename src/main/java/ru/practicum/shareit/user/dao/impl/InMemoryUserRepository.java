package ru.practicum.shareit.user.dao.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.exemption.DuplicationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> get() {
        return users.values();
    }

    @Override
    public User add(User user) {
        emailExistCheck(user.getEmail());
        Long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return users.get(id);
    }

    @Override
    public User update(Long userId, User user) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("User with id %d not found", userId));
        }
        emailExistCheck(user.getEmail());
        User userFromBd = users.get(userId);
        if (user.getName() != null) {
            userFromBd.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromBd.setEmail(user.getEmail());
        }
        users.put(userId, userFromBd);
        return userFromBd;
    }

    @Override
    public Optional<User> getById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }

    private void emailExistCheck(String email) {
        if (users.values().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new DuplicationException("User with email " + email + " already exists");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
