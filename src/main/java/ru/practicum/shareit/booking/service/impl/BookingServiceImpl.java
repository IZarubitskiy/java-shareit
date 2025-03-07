package ru.practicum.shareit.booking.service.impl;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;

    @Override
    public BookingDtoResponse createBooking(BookingDtoRequestCreate bookingDtoRequestCreate, Long bookerId) {
        User booker = userMapper.toUser(userService.getById(bookerId));
        Item item = itemService.getById(bookingDtoRequestCreate.getItemId());

        Booking booking = bookingMapper.toBooking(bookingDtoRequestCreate, item, booker);
        booking.setStatus(StatusBooking.WAITING);
        validateBooking(booking);

        return bookingMapper.toBookingDtoResponse(
                bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse setApproved(Long bookingId, Boolean approved, Long ownerId) {
        Booking bookingFromDb = findBooking(bookingId);
        if (!bookingFromDb.getItem().getOwner().getId().equals(ownerId)) {
            throw new AuthorizationException(String.format("User id=%d is not owner of item id=%d", ownerId, bookingId));
        }
        if (Boolean.TRUE.equals(approved)) {
            bookingFromDb.setStatus(StatusBooking.APPROVED);
        } else {
            bookingFromDb.setStatus(StatusBooking.REJECTED);
        }

        return bookingMapper.toBookingDtoResponse(
                bookingRepository.save(bookingFromDb));
    }

    @Override
    public BookingDtoResponse getBooking(Long bookingId, Long userId) {
        Booking booking = findBooking(bookingId);
        if (!booking.getBooker().getId().equals(userId)
                && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AuthorizationException(String.format("User id=%d doesn't have access to item id=%d", userId, bookingId));
        }
        return bookingMapper.toBookingDtoResponse(booking);
    }

    @Override
    public Collection<BookingDtoResponse> getBookerBookings(Long bookerId, State state) {
        UserDtoResponse booker = userService.getById(bookerId);
        Collection<Booking> bookings;
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByBooker_IdOrderByStartDateDesc(bookerId);
            case CURRENT -> bookings = bookingRepository.findCurrentByBooker_Id(bookerId);
            case PAST -> bookings = bookingRepository.findPastByBooker_Id(bookerId);
            case FUTURE -> bookings = bookingRepository.findFutureByBooker_Id(bookerId);
            case WAITING ->
                    bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDateDesc(bookerId, StatusBooking.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findAllByBooker_IdAndStatusOrderByStartDateDesc(bookerId, StatusBooking.REJECTED);
            default -> throw new IllegalStateException("Unknown state");
        }
        return bookings.stream().map(bookingMapper::toBookingDtoResponse).toList();
    }

    @Override
    public Collection<BookingDtoResponse> getOwnerBookings(Long ownerId, State state) {
        UserDtoResponse user = userService.getById(ownerId);
        Collection<Booking> bookings;
        switch (state) {
            case ALL -> bookings = bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(ownerId);
            case CURRENT -> bookings = bookingRepository.findCurrentByOwner_Id(ownerId);
            case PAST -> bookings = bookingRepository.findPastByOwner_Id(ownerId);
            case FUTURE -> bookings = bookingRepository.findFutureByOwner_Id(ownerId);
            case WAITING ->
                    bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(ownerId, StatusBooking.WAITING);
            case REJECTED ->
                    bookings = bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(ownerId, StatusBooking.REJECTED);
            default -> throw new IllegalStateException("Unknown state");
        }

        return bookings.stream().map(bookingMapper::toBookingDtoResponse).toList();
    }

    private Booking findBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking id=%d not found", bookingId)));
    }

    private void validateBooking(Booking booking) {
        LocalDateTime start = booking.getStartDate();
        LocalDateTime end = booking.getEndDate();

        if (start.isAfter(end)) {
            throw new ValidationException("Start date is after end date");
        } else if (start.equals(end)) {
            throw new ValidationException("Start date equals end date");
        } else if (Boolean.FALSE.equals(booking.getItem().getAvailable())) {
            throw new ValidationException(String.format("Item %d is not available", booking.getItem().getId()));
        }
    }

}