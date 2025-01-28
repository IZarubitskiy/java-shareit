package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDtoCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdateRequest;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemDtoResponse> get(Long userId);

    ItemDtoResponse add(Long userId, ItemDtoCreateRequest itemDtoCreateRequest);

    ItemDtoResponse update(Long userId, Long itemId, ItemDtoUpdateRequest itemDtoUpdateRequest);

    ItemDtoResponse getById(Long itemId);

    List<ItemDtoResponse> getByString(String query);
}
