package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;

class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void testNulls() {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(null, null);
        ItemRequestDtoResponse itemDtoResponse = itemRequestMapper.toItemRequestDtoResponse(null);
        ItemRequestDtoResponseWithAnswers itemRequestDtoResponseWithAnswers = itemRequestMapper.toItemRequestDtoResponseWithAnswers(null, null);
    }

}