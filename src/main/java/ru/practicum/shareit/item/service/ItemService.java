package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemResponse> get(Long userId);

    ItemDto add(Long userId, ItemDto itemDto);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    ItemDto getById(Long itemId);

    List<ItemDto> getByString(String query);
}
