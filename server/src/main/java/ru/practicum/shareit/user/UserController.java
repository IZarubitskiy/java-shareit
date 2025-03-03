package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDtoResponse createUser(@Valid @RequestBody UserDtoRequestCreate userDtoRequestCreate) {
        return userService.add(userDtoRequestCreate);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDtoResponse updateUser(@Valid @RequestBody UserDtoRequestUpdate userDtoRequestUpdate,
                                      @PathVariable Long userId) {
        return userService.update(userId, userDtoRequestUpdate);
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
