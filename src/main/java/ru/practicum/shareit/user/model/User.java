package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
//TODO Sprint add-controllers.
public class User {
    private Long id;
    private String name;
    private String email;
}
