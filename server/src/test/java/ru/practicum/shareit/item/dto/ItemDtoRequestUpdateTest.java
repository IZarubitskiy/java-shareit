package ru.practicum.shareit.item.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoRequestUpdateTest {


    @Autowired
    private JacksonTester<ItemDtoRequestUpdate> jsonTester;

    @Test
    void testUpdateItemRequestSerialization() throws Exception {
        ItemDtoRequestUpdate request = ItemDtoRequestUpdate.builder()
                .name("Updated Item")
                .description("Updated Description")
                .available(false)
                .build();

        JsonContent<ItemDtoRequestUpdate> json = jsonTester.write(request);

        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Updated Item");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Updated Description");
        assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(false);
    }

    @Test
    void testUpdateItemRequestDeserialization() throws Exception {
        String jsonContent = "{\"name\":\"Updated Item\",\"description\":\"Updated Description\",\"available\":false}";

        ItemDtoRequestUpdate request = jsonTester.parseObject(jsonContent);

        assertThat(request.getName()).isEqualTo("Updated Item");
        assertThat(request.getDescription()).isEqualTo("Updated Description");
        assertThat(request.getAvailable()).isEqualTo(false);
    }
}