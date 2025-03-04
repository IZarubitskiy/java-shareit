package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDtoResponse add(ItemRequestDtoRequestCreate itemRequestDtoRequestCreate, Long requesterId);

    List<ItemRequestDtoResponseWithAnswers> get(Long requesterId);

    List<ItemRequestDtoResponse> getAll();

    ItemRequestDtoResponseWithAnswers getById(Long requestId);

}