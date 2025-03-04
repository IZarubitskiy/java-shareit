package ru.practicum.shareit.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
class UserDtoRequestCreateTest {

    @Autowired
    private JacksonTester<UserDtoRequestCreate> userDtoRequestCreateJacksonTester;

    @Test
    void testUserDtoRequestCreateSerialization() throws Exception {
        UserDtoRequestCreate userDtoRequestCreate = UserDtoRequestCreate.builder()
                .name("Test User")
                .email("test@example.com")
                .build();

        JsonContent<UserDtoRequestCreate> json = userDtoRequestCreateJacksonTester.write(userDtoRequestCreate);

        Assertions.assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        Assertions.assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testUserDtoRequestCreateDeserialization() throws Exception {
        String jsonContent = "{\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDtoRequestCreate userDtoRequestCreate = userDtoRequestCreateJacksonTester.parseObject(jsonContent);

        assertThat(userDtoRequestCreate.getName()).isEqualTo("Test User");
        assertThat(userDtoRequestCreate.getEmail()).isEqualTo("test@example.com");
    }

}
