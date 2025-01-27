package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Collection<Item> get();

    Item add(Item user);

    Item update(Item user);

    Optional<Item> getById(Long id);

    boolean delete(Long id);
}
