package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {
    BookingDtoResponse createBooking(BookingDtoRequestCreate bookingDtoRequestCreate, Long bookerId);

    BookingDtoResponse setApproved(Long bookingId, Boolean approved, Long ownerId);

    BookingDtoResponse getBooking(Long bookingId, Long userId);

    Collection<BookingDtoResponse> getBookerBookings(Long bookerId, State state);

    Collection<BookingDtoResponse> getOwnerBookings(Long ownerId, State state);
}