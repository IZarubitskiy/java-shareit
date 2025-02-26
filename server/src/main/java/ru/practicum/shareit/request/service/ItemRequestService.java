package ru.practicum.shareit.request.service;

import ru.practicum.shareit.item.dto.ItemDtoResponseSeek;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDtoResponse add(ItemRequestDtoRequestCreate itemRequestDtoRequestCreate);

    Collection<ItemRequestDtoResponse> get(Long userId);

    Collection<ItemRequestDtoResponse> getAll(Long userId);

    ItemRequestDtoResponse getById(Long userId);

}
