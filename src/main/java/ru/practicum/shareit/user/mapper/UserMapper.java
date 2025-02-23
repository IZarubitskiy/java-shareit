package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.dto.UserDtoCreateRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoUpdateRequest;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface UserMapper {

    User toUserCreate(UserDtoCreateRequest userDtoCreateRequest);

    @Mapping(target = "id", source = "id")
    User toUserUpdate(UserDtoUpdateRequest userDtoUpdateRequest, Long id);

    UserDtoResponse toUserDtoResponse(User user);

    User responseToUser(UserDtoResponse userDtoResponse);

}
