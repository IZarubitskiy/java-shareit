package ru.practicum.shareit.user.dto;

import org.springframework.boot.test.autoconfigure.json.JsonTest;

@JsonTest
class UserDtoRequestCreateTest {
    /*
    @Autowired
    private JacksonTester<UserDtoRequestCreate> createUserRequestJson;

    @Test
    void testCreateUserRequestSerialization() throws Exception {
        UserDtoRequestCreate userDtoRequestCreate = UserDtoRequestCreate.builder()
                .name("Test User")
                .email("test@example.com")
                .build();

        JsonContent<UserDtoRequestCreate> json = createUserRequestJson.write(userDtoRequestCreate);

        Assertions.assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        Assertions.assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testCreateUserRequestDeserialization() throws Exception {
        String jsonContent = "{\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDtoRequestCreate userDtoRequestCreate = createUserRequestJson.parseObject(jsonContent);

        assertThat(userDtoRequestCreate.getName()).isEqualTo("Test User");
        assertThat(userDtoRequestCreate.getEmail()).isEqualTo("test@example.com");
    }
*/
}