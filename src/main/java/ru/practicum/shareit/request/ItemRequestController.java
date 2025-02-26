package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoRequestUpdate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoResponse addRequest(@Valid @RequestBody ItemRequestDtoRequestCreate itemRequestDtoRequestCreate) {
        return itemRequestService.add(itemRequestDtoRequestCreate);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoResponse> get(@Valid @RequestBody UserDtoRequestUpdate userDtoRequestUpdate,
                                                 @PathVariable Long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDtoResponse> geAll(@PathVariable Long userId) {
        return itemRequestService.getAll(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void ItemRequestDtoResponse(@PathVariable Long userId) {
        itemRequestService.getById(userId);
    }
}