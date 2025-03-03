package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.UserDtoResponse;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoResponse {
    private Long id;
    private ItemDtoResponse item;
    private UserDtoResponse booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private StatusBooking status;
}