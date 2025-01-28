package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Collection<Item> get(Long userId);

    Item add(Item item);

    Item update(Long itemId, Item item);

    Optional<Item> getById(Long itemId);

    List<Item> getByString(String query);

}
