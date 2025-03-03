package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDtoResponse add(ItemRequestDtoRequestCreate itemRequestDtoRequestCreate, Long requesterId);

    Collection<ItemRequestDtoResponseWithAnswers> get(Long requesterId);

    Collection<ItemRequestDtoResponse> getAll(Long requesterId);

    ItemRequestDtoResponseWithAnswers getById(Long requestId);

}