package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BookingDtoResponse createBooking(@Valid @RequestBody BookingDtoRequestCreate bookingDtoRequestCreate,
                                     @RequestHeader(value = "X-Sharer-User-Id") Long bookerId) {
        return bookingService.createBooking(bookingDtoRequestCreate, bookerId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    BookingDtoResponse setApproved(@PathVariable Long bookingId,
                                   @RequestParam Boolean approved,
                                   @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return bookingService.setApproved(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    BookingDtoResponse getBooking(@PathVariable Long bookingId,
                                  @RequestHeader(value = "X-Sharer-User-Id") Long userId) {

        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<BookingDtoResponse> getBookerBookings(@RequestHeader(value = "X-Sharer-User-Id") Long bookerId,
                                                     @RequestParam(required = false, defaultValue = "ALL") State state) {
        return bookingService.getBookerBookings(bookerId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    Collection<BookingDtoResponse> getOwnerBookings(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(required = false, defaultValue = "ALL") State state) {
        return bookingService.getOwnerBookings(ownerId, state);
    }
}