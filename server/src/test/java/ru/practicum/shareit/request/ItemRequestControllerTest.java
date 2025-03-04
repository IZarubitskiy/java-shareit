package ru.practicum.shareit.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void testCreateRequest() throws Exception {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("request description test")
                .build();

        ItemRequestDtoResponse itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("request description test")
                .requesterId(1L)
                .created(LocalDateTime.now())
                .build();

        Mockito.when(itemRequestService.add(Mockito.any(ItemRequestDtoRequestCreate.class), Mockito.eq(1L)))
                .thenReturn(itemRequestDtoResponse);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDtoRequestCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("request description test"))
                .andExpect(jsonPath("$.requesterId").value(1L));
    }

    @Test
    void testGetUserRequests() throws Exception {
        ItemDtoResponse item = ItemDtoResponse.builder()
                .id(1L)
                .name("item name test")
                .description("item description test")
                .available(true)
                .build();

        ItemRequestDtoResponseWithAnswers itemRequestDtoResponseWithAnswers = ItemRequestDtoResponseWithAnswers.builder()
                .id(1L)
                .description("request description test")
                .created(LocalDateTime.now())
                .items(Collections.singletonList(item))
                .build();

        List<ItemRequestDtoResponseWithAnswers> requests = Collections.singletonList(itemRequestDtoResponseWithAnswers);

        Mockito.when(itemRequestService.get(Mockito.eq(1L))).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("request description test"))
                .andExpect(jsonPath("$[0].items[0].id").value(1L))
                .andExpect(jsonPath("$[0].items[0].name").value("item name test"))
                .andExpect(jsonPath("$[0].items[0].description").value("item description test"));
    }

    @Test
    void testGetAllRequests() throws Exception {
        ItemRequestDtoResponse itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("request description test")
                .requesterId(1L)
                .created(LocalDateTime.now())
                .build();

        List<ItemRequestDtoResponse> requests = Collections.singletonList(itemRequestDtoResponse);

        Mockito.when(itemRequestService.getAll()).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value("request description test"))
                .andExpect(jsonPath("$[0].requesterId").value(1L));
    }

    @Test
    void testGetRequestById() throws Exception {
        ItemDtoResponse item = ItemDtoResponse.builder()
                .id(1L)
                .name("item name test")
                .description("item description test")
                .available(true)
                .build();

        ItemRequestDtoResponseWithAnswers itemRequestDtoResponseWithAnswers = ItemRequestDtoResponseWithAnswers.builder()
                .id(1L)
                .description("request description test")
                .created(LocalDateTime.now())
                .items(Collections.singletonList(item))
                .build();

        Mockito.when(itemRequestService.getById(Mockito.eq(1L))).thenReturn(itemRequestDtoResponseWithAnswers);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("request description test"))
                .andExpect(jsonPath("$.items[0].id").value(1L))
                .andExpect(jsonPath("$.items[0].name").value("item name test"))
                .andExpect(jsonPath("$.items[0].description").value("item description test"));
    }

}