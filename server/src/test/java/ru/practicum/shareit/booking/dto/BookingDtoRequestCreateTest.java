package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoRequestCreateTest {

    @Autowired
    private JacksonTester<BookingDtoRequestCreate> jsonTester;

    @Test
    void testCreateBookingRequestSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);

        BookingDtoRequestCreate bookingDtoRequestCreate = BookingDtoRequestCreate.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        JsonContent<BookingDtoRequestCreate> json = jsonTester.write(bookingDtoRequestCreate);

        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-01T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-02T12:00:00");
    }

    @Test
    void testCreateBookingRequestDeserialization() throws Exception {
        String jsonContent = "{\"itemId\":1,\"start\":\"2023-10-01T12:00:00\",\"end\":\"2023-10-02T12:00:00\"}";

        BookingDtoRequestCreate bookingDtoRequestCreate = jsonTester.parseObject(jsonContent);

        assertThat(bookingDtoRequestCreate.getItemId()).isEqualTo(1L);
        assertThat(bookingDtoRequestCreate.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 1, 12, 0));
        assertThat(bookingDtoRequestCreate.getEnd()).isEqualTo(LocalDateTime.of(2023, 10, 2, 12, 0));
    }

}