package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.user.UserDtoResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoResponseTest {

    @Autowired
    private JacksonTester<BookingDtoResponse> jsonTester;

    @Test
    void testBookingResponseSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);

        ItemDtoResponse item = ItemDtoResponse.builder()
                .id(1L)
                .name("Test Item")
                .build();

        UserDtoResponse booker = UserDtoResponse.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .item(item)
                .booker(booker)
                .start(start)
                .end(end)
                .status(StatusBooking.APPROVED)
                .build();

        JsonContent<BookingDtoResponse> json = jsonTester.write(bookingDtoResponse);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.item.name").isEqualTo("Test Item");
        assertThat(json).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.booker.name").isEqualTo("Test User");
        assertThat(json).extractingJsonPathStringValue("$.booker.email").isEqualTo("test@example.com");
        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-01T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-02T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }

    @Test
    void testBookingResponseDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"item\":{\"id\":1,\"name\":\"Test Item\"},\"booker\":{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"},\"start\":\"2023-10-01T12:00:00\",\"end\":\"2023-10-02T12:00:00\",\"status\":\"APPROVED\"}";

        BookingDtoResponse response = jsonTester.parseObject(jsonContent);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getItem().getId()).isEqualTo(1L);
        assertThat(response.getItem().getName()).isEqualTo("Test Item");
        assertThat(response.getBooker().getId()).isEqualTo(1L);
        assertThat(response.getBooker().getName()).isEqualTo("Test User");
        assertThat(response.getBooker().getEmail()).isEqualTo("test@example.com");
        assertThat(response.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 1, 12, 0));
        assertThat(response.getEnd()).isEqualTo(LocalDateTime.of(2023, 10, 2, 12, 0));
        assertThat(response.getStatus()).isEqualTo(StatusBooking.APPROVED);
    }

}