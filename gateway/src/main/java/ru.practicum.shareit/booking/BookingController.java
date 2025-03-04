package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingState;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid BookingDtoRequestCreate bookingDtoRequestCreate) {
        log.info("Creating booking {}, userId={}", bookingDtoRequestCreate, userId);
        return bookingClient.createBooking(userId, bookingDtoRequestCreate);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> setApproved(@PathVariable Long bookingId,
                                              @RequestParam Boolean approved,
                                              @RequestHeader(value = "X-Sharer-User-Id") Long ownerId) {
        return bookingClient.setApproved(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookerBookings(@RequestHeader(value = "X-Sharer-User-Id") Long bookerId,
                                                    @RequestParam(required = false, defaultValue = "ALL") BookingState bookingState) {
        return bookingClient.getBookerBookings(bookerId, bookingState);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                   @RequestParam(required = false, defaultValue = "ALL") BookingState bookingState) {
        return bookingClient.getOwnerBookings(ownerId, bookingState);
    }


}