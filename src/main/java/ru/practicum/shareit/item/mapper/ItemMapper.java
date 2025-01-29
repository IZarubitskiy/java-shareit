package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemDtoCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdateRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "itemDtoCreateRequest.name")
    Item toItemCreate(ItemDtoCreateRequest itemDtoCreateRequest, User owner);

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "name", source = "itemDtoUpdateRequest.name")
    Item toItemUpdate(ItemDtoUpdateRequest itemDtoUpdateRequest, User owner);

    ItemDtoResponse toItemDtoResponse(Item item);

}

