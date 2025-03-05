package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;

class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Test
    void testNulls() {
        Booking booking = bookingMapper.toBooking(null, null, null);

        BookingDtoResponse bookingDtoResponse = bookingMapper.toBookingDtoResponse(null, null, null);
    }

}