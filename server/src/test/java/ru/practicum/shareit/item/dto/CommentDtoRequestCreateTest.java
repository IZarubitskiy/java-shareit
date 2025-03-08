package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoRequestCreateTest {
    @Autowired
    private JacksonTester<CommentDtoRequestCreate> jsonTester;

    @Test
    void testCreateCommentRequestSerialization() throws Exception {
        CommentDtoRequestCreate request = CommentDtoRequestCreate.builder()
                .text("Test Comment")
                .build();

        JsonContent<CommentDtoRequestCreate> json = jsonTester.write(request);

        assertThat(json).extractingJsonPathStringValue("$.text").isEqualTo("Test Comment");
    }

    @Test
    void testCreateCommentRequestDeserialization() throws Exception {
        String jsonContent = "{\"text\":\"Test Comment\"}";

        CommentDtoRequestCreate request = jsonTester.parseObject(jsonContent);

        assertThat(request.getText()).isEqualTo("Test Comment");
    }
}