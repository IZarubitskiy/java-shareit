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
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestResponseDto;
import ru.practicum.shareit.request.dto.RequestResponseWithAnswersDto;
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
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();

        RequestResponseDto requestResponseDto = itemRequestService.add(requestCreateDto, userDtoResponse.getId());

        assertThat(requestResponseDto.getId()).isNotNull();
        assertThat(requestResponseDto.getDescription()).isEqualTo("test description");
        assertThat(requestResponseDto.getRequesterId()).isEqualTo(userDtoResponse.getId());

        ItemRequest savedRequest = itemRequestRepository.findById(requestResponseDto.getId()).orElseThrow();
        assertThat(savedRequest.getDescription()).isEqualTo("test description");
        assertThat(savedRequest.getRequester().getId()).isEqualTo(userDtoResponse.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateRequest() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();

        assertThatThrownBy(() -> itemRequestService.add(requestCreateDto, 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldFindAllUserRequests() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();
        RequestResponseDto requestResponse = itemRequestService.add(requestCreateDto, userDtoResponse.getId());

        List<RequestResponseWithAnswersDto> userRequests = itemRequestService.getOwn(userDtoResponse.getId());

        assertThat(userRequests).hasSize(1);
        assertThat(userRequests.getFirst().getId()).isEqualTo(requestResponse.getId());
        assertThat(userRequests.getFirst().getDescription()).isEqualTo("test description");
    }

    @Test
    void shouldFindAllRequests() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();
        RequestResponseDto requestResponse = itemRequestService.add(requestCreateDto, userDtoResponse.getId());

        List<RequestResponseDto> allRequests = itemRequestService.getAll();

        assertThat(allRequests).hasSize(1);
        assertThat(allRequests.getFirst().getId()).isEqualTo(requestResponse.getId());
        assertThat(allRequests.getFirst().getDescription()).isEqualTo("test description");
        assertThat(allRequests.getFirst().getRequesterId()).isEqualTo(userDtoResponse.getId());
    }

    @Test
    void shouldFindRequestById() {
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();
        RequestResponseDto requestResponse = itemRequestService.add(requestCreateDto, userDtoResponse.getId());

        RequestResponseWithAnswersDto foundRequest = itemRequestService.getById(requestResponse.getId());

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
        RequestCreateDto requestCreateDto = RequestCreateDto.builder()
                .description("test description")
                .build();
        RequestResponseDto requestResponseDto = itemRequestService.add(requestCreateDto, userDtoResponse.getId());

        ItemDtoRequestCreate itemDtoRequestCreate = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .requestId(2L)
                .build();
        itemDtoResponse = itemService.add(userDtoResponse.getId(), itemDtoRequestCreate);

        RequestResponseWithAnswersDto foundRequest = itemRequestService.getById(requestResponseDto.getId());

        assertThat(foundRequest.getItems()).hasSize(1);
        assertThat(foundRequest.getId()).isEqualTo(requestResponseDto.getId());
        assertThat(foundRequest.getItems().getFirst().getId()).isEqualTo(itemDtoResponse.getId());
        assertThat(foundRequest.getItems().getFirst().getName()).isEqualTo("test item");
    }

}
