package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "itemDtoRequestCreate.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemRequest", source = "itemRequest")
    @Mapping(target = "description", source = "itemDtoRequestCreate.description")
    Item toItemCreate(ItemDtoRequestCreate itemDtoRequestCreate, User owner, ItemRequest itemRequest);

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "itemDtoRequestUpdate.name")
    @Mapping(target = "id", source = "itemId")
    Item toItemUpdate(ItemDtoRequestUpdate itemDtoRequestUpdate, User owner, Long itemId);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDtoResponse toItemDtoResponse(Item item);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "ownerId", source = "item.owner.id")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "lastBooking", source = "lastBooking")
    ItemDtoResponseSeek toItemDtoResponseSeek(Item item,
                                              List<CommentDtoResponseItem> comments,
                                              LocalDateTime nextBooking,
                                              LocalDateTime lastBooking);

    Item toItem(ItemDtoResponse itemDtoResponse);

}