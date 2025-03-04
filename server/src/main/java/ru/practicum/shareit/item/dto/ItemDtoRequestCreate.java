package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;

@Data
@Builder
public class ItemDtoRequestCreate {
    private String name;
    private String description;
    private Boolean available;

    @Getter(AccessLevel.NONE)
    private Long requestId;

    public Optional<Long> getRequestId() {
        return Optional.ofNullable(requestId);
    }
}