package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoResponse createRequest(@RequestBody ItemRequestDtoRequestCreate itemRequestDtoRequestCreate,
                                                @RequestHeader(name = "X-Sharer-User-Id") Long requesterId) {

        return itemRequestService.add(itemRequestDtoRequestCreate, requesterId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoResponseWithAnswers> get(@RequestHeader(value = "X-Sharer-User-Id") Long requesterId) {
        return itemRequestService.get(requesterId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoResponse> geAll() {
        return itemRequestService.getAll();
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoResponseWithAnswers itemRequestDtoResponseWithAnswers(@PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }
}