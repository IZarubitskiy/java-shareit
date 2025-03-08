package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseWithAnswersDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto createRequest(@RequestBody RequestCreateDto requestCreateDto,
                                            @RequestHeader(name = "X-Sharer-User-Id") Long requesterId) {

        return itemRequestService.add(requestCreateDto, requesterId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestResponseWithAnswersDto> getOwn(@RequestHeader(value = "X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.getOwn(requesterId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<RequestResponseDto> getAll() {
        return itemRequestService.getAll();
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public RequestResponseWithAnswersDto getById(@PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }
}