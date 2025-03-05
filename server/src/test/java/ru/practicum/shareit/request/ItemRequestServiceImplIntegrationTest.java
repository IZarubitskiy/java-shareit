package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:shareit",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=shareit",
        "spring.datasource.password=shareit",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
public class ItemRequestServiceImplIntegrationTest {


    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private UserDtoResponse userDtoResponse;
    private ItemDtoResponse itemDtoResponse;

    @BeforeEach
    void setUp() {
        UserDtoRequestCreate userDtoRequestCreate = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test@example.com")
                .build();
        userDtoResponse = userService.add(userDtoRequestCreate);

        ItemDtoRequestCreate itemDtoRequestCreate = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .build();
        itemDtoResponse = itemService.add(userDtoResponse.getId(), itemDtoRequestCreate);
    }

    @Test
    void shouldCreateRequest() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();

        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());

        assertThat(itemRequestDtoResponse.getId()).isNotNull();
        assertThat(itemRequestDtoResponse.getDescription()).isEqualTo("test description");
        assertThat(itemRequestDtoResponse.getRequesterId()).isEqualTo(userDtoResponse.getId());

        ItemRequest savedRequest = itemRequestRepository.findById(itemRequestDtoResponse.getId()).orElseThrow();
        assertThat(savedRequest.getDescription()).isEqualTo("test description");
        assertThat(savedRequest.getRequester().getId()).isEqualTo(userDtoResponse.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateRequest() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();

        assertThatThrownBy(() -> itemRequestService.add(itemRequestDtoRequestCreate, 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldFindAllUserRequests() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();
        ItemRequestDtoResponse requestResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());

        List<ItemRequestDtoResponseWithAnswers> userRequests = itemRequestService.getOwn(userDtoResponse.getId());

        assertThat(userRequests).hasSize(1);
        assertThat(userRequests.getFirst().getId()).isEqualTo(requestResponse.getId());
        assertThat(userRequests.getFirst().getDescription()).isEqualTo("test description");
    }

    @Test
    void shouldFindAllRequests() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();
        ItemRequestDtoResponse requestResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());

        List<ItemRequestDtoResponse> allRequests = itemRequestService.getAll();

        assertThat(allRequests).hasSize(1);
        assertThat(allRequests.getFirst().getId()).isEqualTo(requestResponse.getId());
        assertThat(allRequests.getFirst().getDescription()).isEqualTo("test description");
        assertThat(allRequests.getFirst().getRequesterId()).isEqualTo(userDtoResponse.getId());
    }

    @Test
    void shouldFindRequestById() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();
        ItemRequestDtoResponse requestResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());

        ItemRequestDtoResponseWithAnswers foundRequest = itemRequestService.getById(requestResponse.getId());

        assertThat(foundRequest.getId()).isEqualTo(requestResponse.getId());
        assertThat(foundRequest.getId()).isEqualTo(requestResponse.getId());
        assertThat(foundRequest.getDescription()).isEqualTo("test description");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRequestNotFound() {
        assertThatThrownBy(() -> itemRequestService.getById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldFindRequestWithItems() {
        ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("test description")
                .build();
        ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());

        ItemDtoRequestCreate itemDtoRequestCreate = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .requestId(1L)
                .build();
        itemDtoResponse = itemService.add(userDtoResponse.getId(), itemDtoRequestCreate);

        ItemRequestDtoResponseWithAnswers foundRequest = itemRequestService.getById(itemRequestDtoResponse.getId());

        assertThat(foundRequest.getItems()).hasSize(1);
        assertThat(foundRequest.getId()).isEqualTo(itemRequestDtoResponse.getId());
        assertThat(foundRequest.getItems().getFirst().getId()).isEqualTo(itemDtoResponse.getId());
        assertThat(foundRequest.getItems().getFirst().getName()).isEqualTo("test item");
    }
}
