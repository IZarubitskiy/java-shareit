package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoRequestCreateTest {


    @Autowired
    private JacksonTester<ItemRequestDtoRequestCreate> jsonTester;

    @Test
    void testRequestCreateRequestSerialization() throws Exception {
        ItemRequestDtoRequestCreate request = ItemRequestDtoRequestCreate.builder()
                .description("Test Description")
                .build();

        JsonContent<ItemRequestDtoRequestCreate> json = jsonTester.write(request);

        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Test Description");
    }

    @Test
    void testRequestCreateRequestDeserialization() throws Exception {
        String jsonContent = "{\"description\":\"Test Description\"}";

        ItemRequestDtoRequestCreate request = jsonTester.parseObject(jsonContent);

        assertThat(request.getDescription()).isEqualTo("Test Description");
    }

}