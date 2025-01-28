package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO Sprint add-controllers.

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDtoCreateRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Description is required.")
    private String description;

    @NotNull(message = "Available is required")
    private Boolean available;
}
