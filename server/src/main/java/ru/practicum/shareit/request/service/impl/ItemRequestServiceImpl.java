package ru.practicum.shareit.request.service.impl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private ItemRequestRepository itemRequestRepository;
    private UserService userService;
    private ItemService itemService;
    private ItemRequestMapper itemRequestMapper;
    private UserMapper userMapper;

    @Override
    public ItemRequestDtoResponse add(ItemRequestDtoRequestCreate itemRequestDtoRequestCreate, Long requesterId) {
        User requester = userMapper.toUser(userService.getById(requesterId));
        ItemRequest request = itemRequestMapper.toItemRequest(itemRequestDtoRequestCreate, requester);
        request.setCreationDate(LocalDateTime.now());
        return itemRequestMapper.toItemRequestDtoResponse(itemRequestRepository.save(request));
    }

    @Override
    public List<ItemRequestDtoResponseWithAnswers> get(Long requesterId) {
        List<ItemRequest> userRequests = itemRequestRepository.findRequestsByRequester_IdOrderByCreationDateDesc(requesterId);
        List<ItemDtoResponse> answers = itemService.getItemsByRequestIds(userRequests.stream().map(ItemRequest::getId).toList());

        Map<Long, List<ItemDtoResponse>> answersByRequestId = answers.stream().collect(Collectors.groupingBy(ItemDtoResponse::getRequestId));

        return userRequests.stream()
                .map(request ->
                        itemRequestMapper.toItemRequestDtoResponseWithAnswers(
                                request,
                                answersByRequestId.getOrDefault(request.getId(), List.of())
                        )
                ).toList();
    }

    @Override
    public List<ItemRequestDtoResponse> getAll() {
        return itemRequestRepository.findAllOrderByCreationDateDesc().stream()
                .map(itemRequestMapper::toItemRequestDtoResponse)
                .toList();
    }

    @Override
    public ItemRequestDtoResponseWithAnswers getById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("ItemRequest with id %d not found", requestId)));
        List<ItemDtoResponse> itemDtoResponses = itemService.getItemsByRequestIds(List.of(requestId));
        return itemRequestMapper.toItemRequestDtoResponseWithAnswers(itemRequest, itemDtoResponses);
    }
}