package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoResponseTest {

    @Autowired
    private JacksonTester<UserDtoResponse> userDtoResponseJacksonTester;

    @Test
    void testUserDtoResponseSerialization() throws Exception {
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        JsonContent<UserDtoResponse> json = userDtoResponseJacksonTester.write(userDtoResponse);

        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testUserDtoResponseDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDtoResponse userDtoResponse = userDtoResponseJacksonTester.parseObject(jsonContent);

        assertThat(userDtoResponse.getId()).isEqualTo(1L);
        assertThat(userDtoResponse.getName()).isEqualTo("Test User");
        assertThat(userDtoResponse.getEmail()).isEqualTo("test@example.com");
    }
}