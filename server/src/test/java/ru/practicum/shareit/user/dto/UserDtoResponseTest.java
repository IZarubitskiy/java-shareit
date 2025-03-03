package ru.practicum.shareit.user.dto;

class UserDtoResponseTest {/*

    @Autowired
    private JacksonTester<UserDtoResponse> userResponseJson;

    @Test
    void testUserResponseSerialization() throws Exception {
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();

        JsonContent<UserDtoResponse> json = userResponseJson.write(userDtoResponse);

        Assertions.assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        Assertions.assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("Test User");
        Assertions.assertThat(json).extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    void testUserResponseDeserialization() throws Exception {
        String jsonContent = "{\"id\":1,\"name\":\"Test User\",\"email\":\"test@example.com\"}";

        UserDtoResponse userDtoResponse = userResponseJson.parseObject(jsonContent);

        assertThat(userDtoResponse.getId()).isEqualTo(1L);
        assertThat(userDtoResponse.getName()).isEqualTo("Test User");
        assertThat(userDtoResponse.getEmail()).isEqualTo("test@example.com");
    }*/
}