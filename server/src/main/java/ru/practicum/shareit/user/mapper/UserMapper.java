package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface UserMapper {

    User toUserCreate(UserDtoRequestCreate userDtoRequestCreate);

    @Mapping(target = "id", source = "id")
    User toUserUpdate(UserDtoRequestUpdate userDtoRequestUpdate, Long id);

    UserDtoResponse toUserDtoResponse(User user);

    User toUser(UserDtoResponse userDtoResponse);

}
