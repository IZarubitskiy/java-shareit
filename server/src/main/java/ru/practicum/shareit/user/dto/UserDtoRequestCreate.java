package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRequestCreate {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Email is required.")
    @Email(message = "Email is not valid.")
    private String email;
}
