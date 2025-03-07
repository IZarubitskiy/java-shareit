package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoResponse {
    private Long id;
    private String text;
    private ItemDtoResponse item;
    private String authorName;
    private LocalDateTime created;
}
