package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemRequestMapper {
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "id", ignore = true)
    ItemRequest toItemRequest(ItemRequestDtoRequestCreate createItemRequest, User requester);

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "created", source = "request.creationDate")
    ItemRequestDtoResponse toItemRequestDtoResponse(ItemRequest request);

    @Mapping(target = "items", source = "answers")
    @Mapping(target = "created", source = "request.creationDate")
    ItemRequestDtoResponseWithAnswers toItemRequestDtoResponseWithAnswers(ItemRequest request, List<ItemDtoResponse> answers);
}