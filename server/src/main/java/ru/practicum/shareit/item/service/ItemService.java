package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDtoResponseSeek> get(Long userId);

    ItemDtoResponse add(Long userId, ItemDtoRequestCreate itemDtoRequestCreate);

    ItemDtoResponse update(Long userId, Long itemId, ItemDtoRequestUpdate itemDtoRequestUpdate);

    Item getById(Long itemId);

    ItemDtoResponseSeek getItemWithCommentsById(Long itemId);

    List<ItemDtoResponse> getByString(String query);

    CommentDtoResponse addComment(CommentDtoRequestCreate commentDtoRequestCreate, Long itemId, Long authorId);

    List<ItemDtoResponse> getItemsByRequestIds(List<Long> requestIds);

}