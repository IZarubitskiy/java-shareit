package ru.practicum.shareit.user.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoRequestUpdateTest {/*

    @Autowired
    private JacksonTester<UserDtoRequestUpdate> updateUserRequestJson;

    @Test
    void testUpdateUserRequestSerialization() throws Exception {
        UserDtoRequestUpdate userDtoRequestUpdate = UserDtoRequestUpdate.builder()
                .name("Updated Test User")
                .email("updated_test@example.com")
                .build();

        JsonContent<UserDtoRequestUpdate> json = updateUserRequestJson.write(userDtoRequestUpdate);

        Assertions.assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Updated Test User");
        Assertions.assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("updated_test@example.com");
    }

    @Test
    void testUpdateUserRequestDeserialization() throws Exception {
        String jsonContent = "{\"name\":\"Updated Test User\",\"email\":\"updated_test@example.com\"}";

        UserDtoRequestUpdate userDtoRequestUpdate = updateUserRequestJson.parseObject(jsonContent);

        assertThat(userDtoRequestUpdate.getName()).isEqualTo("Updated Test User");
        assertThat(userDtoRequestUpdate.getEmail()).isEqualTo("updated_test@example.com");
    }
*/
}