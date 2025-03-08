package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "id", ignore = true)
    ItemRequest toItemRequest(RequestCreateDto createItemRequest, User requester);

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "created", source = "request.creationDate")
    RequestResponseDto toItemRequestDtoResponse(ItemRequest request);

    @Mapping(target = "items", source = "answers")
    @Mapping(target = "created", source = "request.creationDate")
    RequestResponseWithAnswersDto toItemRequestDtoResponseWithAnswers(ItemRequest request, List<ItemDtoResponse> answers);
}