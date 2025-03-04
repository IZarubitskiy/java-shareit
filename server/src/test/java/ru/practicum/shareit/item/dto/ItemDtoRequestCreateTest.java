package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoRequestCreateTest {


    @Autowired
    private JacksonTester<ItemDtoRequestCreate> jsonTester;

    @Test
    void testCreateItemRequestSerialization() throws Exception {
        ItemDtoRequestCreate request = ItemDtoRequestCreate.builder()
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .requestId(123L)
                .build();

        JsonContent<ItemDtoRequestCreate> json = jsonTester.write(request);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test Item");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Test Description");
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(123);
    }

    @Test
    void testCreateItemRequestDeserialization() throws Exception {
        String jsonContent = "{\"name\":\"Test Item\",\"description\":\"Test Description\",\"available\":true,\"requestId\":123}";

        ItemDtoRequestCreate request = jsonTester.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("Test Item");
        assertThat(request.getDescription()).isEqualTo("Test Description");
        assertThat(request.getAvailable()).isEqualTo(true);
        assertThat(request.getRequestId()).isEqualTo(Optional.of(123L));
    }
}