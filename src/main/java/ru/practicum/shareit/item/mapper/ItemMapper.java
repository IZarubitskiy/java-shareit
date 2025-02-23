package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "itemDtoCreateRequest.name")
    Item toItemCreate(ItemDtoCreateRequest itemDtoCreateRequest, User owner);

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "updateItemRequest.name")
    @Mapping(target = "id", source = "id")
    Item toItemUpdate(ItemDtoUpdateRequest itemDtoUpdateRequest, User owner, Long itemId);

    ItemDtoResponse toItemDtoResponse(Item item);

    Item responseToItem(ItemDtoResponse itemDtoResponse, User user);

    ItemWithCommentsDtoResponse toItemWithCommentsDtoRespoonse(ItemDtoResponse itemDtoResponse, List<CommentItemDtoResponse> comments);

}

