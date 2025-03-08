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
    void testBookingDtoRequestCreatetSerialization() throws Exception {
        LocalDateTime start = LocalDateTime.of(2023, 10, 1, 12, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 2, 12, 0);

        BookingDtoRequestCreate request = BookingDtoRequestCreate.builder()
                .itemId(1L)
                .start(start)
                .end(end)
                .build();

        JsonContent<BookingDtoRequestCreate> json = jsonTester.write(request);

        assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo("2023-10-01T12:00:00");
        assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo("2023-10-02T12:00:00");
    }

    @Test
    void testBookingDtoRequestCreateDeserialization() throws Exception {
        String jsonContent = "{\"itemId\":1,\"start\":\"2023-10-01T12:00:00\",\"end\":\"2023-10-02T12:00:00\"}";

        BookingDtoRequestCreate request = jsonTester.parseObject(jsonContent);

        assertThat(request.getItemId()).isEqualTo(1L);
        assertThat(request.getStart()).isEqualTo(LocalDateTime.of(2023, 10, 1, 12, 0));
        assertThat(request.getEnd()).isEqualTo(LocalDateTime.of(2023, 10, 2, 12, 0));
    }
}