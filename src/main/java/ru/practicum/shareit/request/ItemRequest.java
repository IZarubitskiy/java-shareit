package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

@Data
@Builder
// TODO Sprint add-item-requests.
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private Instant created;
}
