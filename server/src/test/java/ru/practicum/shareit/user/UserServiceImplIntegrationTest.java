package ru.practicum.shareit.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.exemption.DuplicationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=shareit",
        "spring.datasource.password=shareit",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class UserServiceImplIntegrationTest {


    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    private final UserDtoRequestCreate userDtoRequestCreate;
    private final UserDtoRequestUpdate userDtoRequestUpdate;

    public UserServiceImplIntegrationTest() {
        userDtoRequestCreate = UserDtoRequestCreate.builder()
                .name("test name")
                .email("test@example.com")
                .build();

        userDtoRequestUpdate = UserDtoRequestUpdate.builder()
                .name("update name")
                .email("update@example.com")
                .build();
    }

    @Test
    void shouldCreateUser() {
        UserDtoResponse userDtoResponse = userService.add(userDtoRequestCreate);

        assertThat(userDtoResponse.getId()).isNotNull();
        assertThat(userDtoResponse.getName()).isEqualTo("test name");
        assertThat(userDtoResponse.getEmail()).isEqualTo("test@example.com");

        User savedUser = userRepository.findById(userDtoResponse.getId()).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("test name");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenCreatingUserWithExistingEmail() {
        userService.add(userDtoRequestCreate);

        UserDtoRequestCreate duplicateEmailRequest = UserDtoRequestCreate.builder()
                .name("another name")
                .email("test@example.com")
                .build();
        Assertions.assertThatThrownBy(() -> userService.add(duplicateEmailRequest))
                .isInstanceOf(DuplicationException.class);
    }

    @Test
    void shouldUpdateUser() {
        UserDtoResponse createdUser = userService.add(userDtoRequestCreate);
        UserDtoResponse updatedUser = userService.update(createdUser.getId(), userDtoRequestUpdate);

        assertThat(updatedUser.getId()).isEqualTo(createdUser.getId());
        assertThat(updatedUser.getName()).isEqualTo("update name");
        assertThat(updatedUser.getEmail()).isEqualTo("update@example.com");

        User savedUser = userRepository.findById(updatedUser.getId()).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo("update name");
        assertThat(savedUser.getEmail()).isEqualTo("update@example.com");
    }

    @Test
    void shouldThrowDataIntegrityViolationWhenUpdatingUserWithExistingEmail() {
        UserDtoResponse firstUser = userService.add(userDtoRequestCreate);

        UserDtoRequestCreate secondUserRequest = UserDtoRequestCreate.builder()
                .name("second name")
                .email("second@example.com")
                .build();
        userService.add(secondUserRequest);

        UserDtoRequestUpdate updateRequestWithExistingEmail = UserDtoRequestUpdate.builder()
                .email("second@example.com")
                .build();

        Assertions.assertThatThrownBy(() -> userService.update(firstUser.getId(), updateRequestWithExistingEmail))
                .isInstanceOf(DuplicationException.class);
    }

    @Test
    void shouldGetUser() {
        UserDtoResponse createdUser = userService.add(userDtoRequestCreate);
        UserDtoResponse foundUser = userService.getById(createdUser.getId());

        assertThat(foundUser.getId()).isEqualTo(createdUser.getId());
        assertThat(foundUser.getName()).isEqualTo("test name");
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingNonExistentUser() {
        Assertions.assertThatThrownBy(() -> userService.getById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldDeleteUser() {
        UserDtoResponse createdUser = userService.add(userDtoRequestCreate);
        userService.delete(createdUser.getId());

        Assertions.assertThatThrownBy(() -> userService.getById(createdUser.getId()))
                .isInstanceOf(NotFoundException.class);
    }

}