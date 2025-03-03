package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoRequestCreate {
    private Long itemId;

    @FutureOrPresent(message = "Start date must be present or in the future")
    private LocalDateTime start;

    @Future(message = "End date must be in the future")
    private LocalDateTime end;
}