package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseWithAnswersDto;

import java.util.List;

public interface ItemRequestService {
    RequestResponseDto add(RequestCreateDto requestCreateDto, Long requesterId);

    List<RequestResponseWithAnswersDto> getOwn(Long requesterId);

    List<RequestResponseDto> getAll();

    RequestResponseWithAnswersDto getById(Long requestId);

}