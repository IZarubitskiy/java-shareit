package ru.practicum.shareit.request.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDtoResponse add(ItemRequestDtoRequestCreate itemRequestDtoRequestCreate) {
        return null;
    }

    @Override
    public Collection<ItemRequestDtoResponse> get(Long userId) {
        return List.of();
    }

    @Override
    public Collection<ItemRequestDtoResponse> getAll(Long userId) {
        return List.of();
    }

    @Override
    public ItemRequestDtoResponse getById(Long userId) {
        return null;
    }
}
