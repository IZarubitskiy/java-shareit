package ru.practicum.shareit.user.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private User updatedUser;
    private UserDtoRequestCreate userDtoRequestCreate;
    private UserDtoRequestUpdate userDtoRequestUpdate;
    private UserDtoResponse userDtoResponseCreated;
    private UserDtoResponse userDtoResponseUpdated;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test Name");
        user.setEmail("test@example.com");

        userDtoRequestCreate = UserDtoRequestCreate.builder().name("Test Name").email("test@example.com").build();
        userDtoRequestUpdate = UserDtoRequestUpdate.builder().name("Updated Name").email("updated@example.com").build();
        userDtoResponseCreated = UserDtoResponse.builder().id(1L).name("Test Name").email("test@example.com").build();

        updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@example.com");
        userDtoResponseUpdated = UserDtoResponse.builder().id(1L).name("Updated Name").email("updated@example.com").build();
    }

    @Test
    void shouldCreateUser() {
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
        UserDtoResponse actualResponse = userService.add(userDtoRequestCreate);
        assertThat(actualResponse).isEqualTo(userDtoResponseCreated);
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }
/*
    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenCreatingUserWithExistingEmail() {
        Mockito.when(userRepository.save(ArgumentMatchers.any(String.class))).thenReturn(true);
        AssertionsForClassTypes.assertThatThrownBy(() -> userService.add(userDtoRequestCreate))
                .isInstanceOf(EmailAlreadyExistsException.class);
        Mockito.verify(userRepository, Mockito.never()).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(ArgumentMatchers.any(String.class));
    }*/

    @Test
    void shouldUpdateUser() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(updatedUser);
        UserDtoResponse actualResponse = userService.update(user.getId(), userDtoRequestUpdate);
        assertThat(actualResponse).isEqualTo(userDtoResponseUpdated);
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
    }
/*
    @Test
    void shouldThrowEmailAlreadyExistsExceptionWhenUpdatingUserWithExistingEmail() {
        Mockito.when(userRepository.existsByEmail(ArgumentMatchers.any(String.class))).thenReturn(true);
        AssertionsForClassTypes.assertThatThrownBy(() -> userService.update(user.getId(), userDtoRequestUpdate))
                .isInstanceOf(DuplicationException.class);
        Mockito.verify(userRepository, Mockito.never()).findById(ArgumentMatchers.anyLong());
        Mockito.verify(userRepository, Mockito.never()).save(ArgumentMatchers.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).existsByEmail(ArgumentMatchers.any(String.class));
    }*/

    @Test
    void shouldFindUser() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        UserDtoResponse actualResponse = userService.getById(user.getId());
        assertThat(actualResponse).isEqualTo(userDtoResponseCreated);
        Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingNonExistingUser() {
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        AssertionsForClassTypes.assertThatThrownBy(() -> userService.update(1L, userDtoRequestUpdate))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldDeleteUser() {
        userService.delete(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(user.getId());
    }
}