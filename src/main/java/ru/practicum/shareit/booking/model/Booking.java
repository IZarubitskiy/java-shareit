package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
@Builder
//TODO Sprint add-bookings.
public class Booking {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private StatusBooking statusBooking;


}
