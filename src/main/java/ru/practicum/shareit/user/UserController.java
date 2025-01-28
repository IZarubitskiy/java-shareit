package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDtoCreateRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdateRequest;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

// TODO Sprint add-controllers.

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse addUser(@Valid @RequestBody UserDtoCreateRequest userDtoCreateRequest) {
        return userService.add(userDtoCreateRequest);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse updateUser(@Valid @RequestBody UserDtoUpdateRequest userDtoUpdateRequest,
                                      @PathVariable Long userId) {
        return userService.update(userId, userDtoUpdateRequest);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse getUser(@PathVariable Long userId) {
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
    }
}
