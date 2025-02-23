package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Collection<ItemWithCommentsDtoResponse> get(Long userId);

    ItemDtoResponse add(Long userId, ItemDtoCreateRequest itemDtoCreateRequest);

    ItemDtoResponse update(Long userId, Long itemId, ItemDtoUpdateRequest itemDtoUpdateRequest);

    Item getById(Long itemId);

    ItemWithCommentsDtoResponse getItemWithCommentsById(Long itemId);

    List<ItemDtoResponse> getByString(String query);

    CommentDtoCreateResponse addComment(CommentDtoCreateRequest commentDtoCreateRequest, Long itemId, Long authorId);

}
