package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
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
    public ItemDtoResponse createItem(@Valid @RequestBody ItemDtoRequestCreate itemDtoRequestCreate,
                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.add(ownerId, itemDtoRequestCreate);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponse updateItem(@Valid @RequestBody ItemDtoRequestUpdate itemDtoRequestUpdate,
                                      @PathVariable Long itemId,
                                      @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.update(ownerId, itemId, itemDtoRequestUpdate);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoResponseComment getItem(@PathVariable(required = false) Long itemId) {
        return itemService.getItemWithCommentsById(itemId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemDtoResponseBooking> getAllUserItems(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemService.get(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDtoResponse> searchItems(@RequestParam String text) {
        return itemService.getByString(text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDtoResponse addComment(@Valid @RequestBody CommentDtoRequestCreate commentDtoRequestCreate,
                                         @PathVariable Long itemId,
                                         @RequestHeader(value = "X-Sharer-User-Id") Long authorId) {
        return itemService.addComment(commentDtoRequestCreate, itemId, authorId);
    }
}
