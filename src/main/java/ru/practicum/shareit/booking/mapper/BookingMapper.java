package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingDtoCreateRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDate", source = "bookingDtoCreateRequest.start")
    @Mapping(target = "endDate", source = "bookingDtoCreateRequest.end")
    Booking toBooking(BookingDtoCreateRequest bookingDtoCreateRequest, Item item, User booker);

    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "start", source = "booking.startDate")
    @Mapping(target = "end", source = "booking.endDate")
    @Mapping(target = "id", source = "booking.id")
    BookingDtoResponse toBookingDtoResponse(Booking booking);
}