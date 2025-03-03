package ru.practicum.shareit.user;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

// надо применить Before Each

@WebMvcTest(UserController.class)
class UserControllerTest {/*

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testCreateUser() throws Exception {
        UserDtoRequestCreate userDtoRequestCreate = UserDtoRequestCreate.builder()
                .name("test name")
                .email("test@example.com")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("test name")
                .email("test@example.com")
                .build();

        Mockito.when(userService.add(Mockito.any(UserDtoRequestCreate.class))).thenReturn(userDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRequestCreate)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDtoRequestUpdate userDtoRequestUpdate = UserDtoRequestUpdate.builder()
                .name("updated test name")
                .email("updated_test@example.com")
                .build();

        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("updated test name")
                .email("updated_test@example.com")
                .build();

        Mockito.when(userService.update(Mockito.eq(1L), Mockito.any(UserDtoRequestUpdate.class))).thenReturn(userDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDtoRequestUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("updated test name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("updated_test@example.com"));
    }

    @Test
    void testGetUser() throws Exception {
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("test name")
                .email("test@example.com")
                .build();

        Mockito.when(userService.getById(1L)).thenReturn(userDtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
*/
}