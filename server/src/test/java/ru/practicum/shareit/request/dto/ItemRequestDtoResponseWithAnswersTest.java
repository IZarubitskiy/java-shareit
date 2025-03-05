package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoResponseWithAnswersTest {

    @Autowired
    private JacksonTester<ItemRequestDtoResponseWithAnswers> itemRequestDtoResponseWithAnswersJacksonTester;

    @Test
    void testItemRequestDtoResponseWithAnswersSerialization() throws Exception {
        LocalDateTime createdAt = LocalDateTime.of(2023, 10, 1, 12, 0);
        List<ItemDtoResponse> items = List.of(
                new ItemDtoResponse(1L, "Item 1 test", "description 1 test", false, 1L, 1L),
                new ItemDtoResponse(2L, "Item 2 test", "description 2 test", true, 2L, 2L)
        );

        ItemRequestDtoResponseWithAnswers response = ItemRequestDtoResponseWithAnswers.builder()
                .id(1L)
                .description("Test Description")
                .created(createdAt)
                .items(items)
                .build();

        JsonContent<ItemRequestDtoResponseWithAnswers> json = itemRequestDtoResponseWithAnswersJacksonTester.write(response);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Test Description");
        assertThat(json).extractingJsonPathStringValue("$.created").isEqualTo("2023-10-01T12:00:00");
        assertThat(json).extractingJsonPathArrayValue("$.items").hasSize(2);
        assertThat(json).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Item 1 test");
        assertThat(json).extractingJsonPathNumberValue("$.items[1].id").isEqualTo(2);
        assertThat(json).extractingJsonPathStringValue("$.items[1].name").isEqualTo("Item 2 test");
    }

    @Test
    void testItemRequestDtoResponseWithAnswersDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"description\":\"Test Description\",\"created\":\"2023-10-01T12:00:00\",\"items\":[{\"id\":1,\"name\":\"Item 1\"},{\"id\":2,\"name\":\"Item 2\"}]}";

        ItemRequestDtoResponseWithAnswers response = itemRequestDtoResponseWithAnswersJacksonTester.parseObject(jsonContent);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDescription()).isEqualTo("Test Description");
        assertThat(response.getCreated()).isEqualTo(LocalDateTime.of(2023, 10, 1, 12, 0));
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getItems().get(0).getId()).isEqualTo(1L);
        assertThat(response.getItems().get(0).getName()).isEqualTo("Item 1");
        assertThat(response.getItems().get(1).getId()).isEqualTo(2L);
        assertThat(response.getItems().get(1).getName()).isEqualTo("Item 2");
    }
}