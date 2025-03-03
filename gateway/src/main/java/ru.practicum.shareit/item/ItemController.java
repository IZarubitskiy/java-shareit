package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;

@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@Valid @RequestBody ItemDtoRequestCreate itemDtoRequestCreate,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.createItem(itemDtoRequestCreate, ownerId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemDtoRequestUpdate itemDtoRequestUpdate,
                                             @PathVariable Long itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.updateItem(itemDtoRequestUpdate, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItem(@PathVariable(required = false) Long itemId,
                                          @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.getItem(itemId, ownerId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.getAllUserItems(ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return itemClient.searchItems(text, ownerId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentDtoRequestCreate commentDtoRequestCreate,
                                             @PathVariable Long itemId,
                                             @RequestHeader(value = "X-Sharer-User-Id") Long authorId) {
        return itemClient.addComment(commentDtoRequestCreate, authorId, itemId);
    }
}