package ru.practicum.shareit.item.mapper;


import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoResponseSeek;
import ru.practicum.shareit.item.model.Item;

public class ItemMapperTest {
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void testNulls() {
        Item item;
        item = itemMapper.toItemCreate(null, null, null);
        item = itemMapper.toItemUpdate(null, null, null);
        ItemDtoResponse itemResponse = itemMapper.toItemDtoResponse(null);
        ItemDtoResponseSeek itemResponseBookingComments = itemMapper.toItemDtoResponseSeek(null, null, null, null);
    }

}