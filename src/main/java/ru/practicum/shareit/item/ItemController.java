package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDtoCreateRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoUpdateRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoResponse createItem(@Valid @RequestBody ItemDtoCreateRequest itemDtoCreateRequest,
                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.add(ownerId, itemDtoCreateRequest);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse updateItem(@Valid @RequestBody ItemDtoUpdateRequest itemDtoUpdateRequest,
                                      @PathVariable Long itemId,
                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.update(ownerId, itemId, itemDtoUpdateRequest);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse getItem(@PathVariable(required = false) Long itemId) {
        return itemService.getById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoResponse> getAllUserItems(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.get(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoResponse> searchItems(@RequestParam String text) {
        return itemService.getByString(text);
    }
}
