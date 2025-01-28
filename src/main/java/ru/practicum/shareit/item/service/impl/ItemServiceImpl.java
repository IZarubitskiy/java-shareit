package ru.practicum.shareit.item.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemResponse> get(Long userId) {
        return itemRepository.get(userId).stream().map(ItemMapper::toItemResponse).toList();
    }

    @Override
    public ItemDto add(Long userId, ItemDto itemDto) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.add(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.update(itemId, item));
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", itemId)));

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getByString(String query) {
        return itemRepository.getByString(query).stream().map(ItemMapper::toItemDto).toList();
    }
}
