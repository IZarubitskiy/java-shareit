package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ItemDtoResponseComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private List<CommentDtoResponseItem> comments;
}
