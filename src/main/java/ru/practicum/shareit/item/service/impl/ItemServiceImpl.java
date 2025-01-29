package ru.practicum.shareit.item.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdateRequest;
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
    private final ItemMapper itemMapper;

    @Override
    public Collection<ItemDtoResponse> get(Long userId) {
        return itemRepository.get(userId).stream().map(itemMapper::toItemDtoResponse).toList();
    }

    @Override
    public ItemDtoResponse add(Long userId, ItemDtoCreateRequest itemDtoCreateRequest) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = itemMapper.toItemCreate(itemDtoCreateRequest, user);
        return itemMapper.toItemDtoResponse(itemRepository.add(item));
    }

    @Override
    public ItemDtoResponse update(Long userId, Long itemId, ItemDtoUpdateRequest itemDtoUpdateRequest) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = itemMapper.toItemUpdate(itemDtoUpdateRequest, user);
        return itemMapper.toItemDtoResponse(itemRepository.update(itemId, item));
    }

    @Override
    public ItemDtoResponse getById(Long itemId) {
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", itemId)));

        return itemMapper.toItemDtoResponse(item);
    }

    @Override
    public List<ItemDtoResponse> getByString(String query) {
        return itemRepository.getByString(query).stream().map(itemMapper::toItemDtoResponse).toList();
    }
}
