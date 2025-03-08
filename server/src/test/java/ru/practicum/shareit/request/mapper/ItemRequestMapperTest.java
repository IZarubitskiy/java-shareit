package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseWithAnswersDto;
import ru.practicum.shareit.request.model.ItemRequest;

class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void testNulls() {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(null, null);
        RequestResponseDto itemDtoResponse = itemRequestMapper.toItemRequestDtoResponse(null);
        RequestResponseWithAnswersDto requestResponseWithAnswersDto = itemRequestMapper.toItemRequestDtoResponseWithAnswers(null, null);
    }

}