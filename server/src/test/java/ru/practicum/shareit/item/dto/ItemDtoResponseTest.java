package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoResponseTest {

    @Autowired
    private JacksonTester<ItemDtoResponse> jsonTester;

    @Test
    void testItemResponseSerialization() throws Exception {
        ItemDtoResponse response = ItemDtoResponse.builder()
                .id(1L)
                .name("Item 1")
                .description("Description 1")
                .available(true)
                .ownerId(112L)
                .requestId(221L)
                .build();

        JsonContent<ItemDtoResponse> json = jsonTester.write(response);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Item 1");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Description 1");
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(json).extractingJsonPathNumberValue("$.ownerId").isEqualTo(112);
        assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(221);
    }

    @Test
    void testItemResponseDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Item 1\",\"description\":\"Description 1\",\"available\":true,\"ownerId\":123,\"requestId\":456}";

        ItemDtoResponse response = jsonTester.parseObject(jsonContent);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Item 1");
        assertThat(response.getDescription()).isEqualTo("Description 1");
        assertThat(response.getAvailable()).isEqualTo(true);
        assertThat(response.getOwnerId()).isEqualTo(123L);
        assertThat(response.getRequestId()).isEqualTo(456L);
    }

}