package ru.practicum.shareit.request;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoRequestCreate;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponseWithAnswers;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Spy
    private ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final User user;
    private final UserDtoResponse userDtoResponse;
    private final ItemRequest itemRequest;
    private final ItemRequestDtoRequestCreate itemRequestDtoRequestCreate;
    private final ItemRequestDtoResponse itemRequestDtoResponse;
    private final ItemRequestDtoResponseWithAnswers requestWithAnswersResponse;
    private final ItemDtoResponse itemDtoResponse;

    public ItemRequestServiceImplTest() {
        user = new User();
        user.setId(1L);
        user.setName("user name test");
        user.setEmail("user@test.com");

        userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("user name test")
                .email("user@test.com")
                .build();

        itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("itemRequest description test");
        itemRequest.setRequester(user);
        itemRequest.setCreationDate(LocalDateTime.now());

        itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                .description("itemRequest description test")
                .build();

        itemRequestDtoResponse = ItemRequestDtoResponse.builder()
                .id(1L)
                .description("itemRequest description test")
                .requesterId(1L)
                .created(LocalDateTime.now())
                .build();

        itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name("item name test")
                .description("item description test")
                .available(true)
                .requestId(1L)
                .build();

        requestWithAnswersResponse = ItemRequestDtoResponseWithAnswers.builder()
                .id(1L)
                .description("itemRequest description test")
                .created(LocalDateTime.now())
                .items(List.of(itemDtoResponse))
                .build();
    }

    @Test
    void shouldCreateRequest() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);

        ItemRequestDtoResponse actualResponse = itemRequestService.add(itemRequestDtoRequestCreate, 1L);

        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(itemRequestDtoResponse);

        verify(userService, times(1)).getById(1L);
        verify(itemRequestRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateRequest() {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException(""));

        assertThatThrownBy(() -> itemRequestService.add(itemRequestDtoRequestCreate, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemRequestRepository, never()).save(any(ItemRequest.class));
    }

    @Test
    void shouldFindAllUserRequests() {
        when(itemRequestRepository.findRequestsByRequester_IdOrderByCreationDateDesc(anyLong())).thenReturn(List.of(itemRequest));
        when(itemService.getItemsByRequestIds(anyList())).thenReturn(List.of(itemDtoResponse));

        List<ItemRequestDtoResponseWithAnswers> actualResponses = itemRequestService.getOwn(1L);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("created")
                .containsExactlyInAnyOrder(requestWithAnswersResponse);

        verify(itemRequestRepository, times(1)).findRequestsByRequester_IdOrderByCreationDateDesc(1L);
        verify(itemService, times(1)).getItemsByRequestIds(List.of(1L));
    }

    @Test
    void shouldFindAllRequests() {
        when(itemRequestRepository.findAllOrderByCreationDateDesc()).thenReturn(List.of(itemRequest));

        List<ItemRequestDtoResponse> actualResponses = itemRequestService.getAll();

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("created")
                .containsExactlyInAnyOrder(itemRequestDtoResponse);

        verify(itemRequestRepository, times(1)).findAllOrderByCreationDateDesc();
    }

    @Test
    void shouldFindRequestById() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemService.getItemsByRequestIds(anyList())).thenReturn(List.of(itemDtoResponse));

        ItemRequestDtoResponseWithAnswers actualResponse = itemRequestService.getById(1L);

        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("created")
                .isEqualTo(requestWithAnswersResponse);

        verify(itemRequestRepository, times(1)).findById(1L);
        verify(itemService, times(1)).getItemsByRequestIds(List.of(1L));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRequestNotFound() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemRequestService.getById(1L))
                .isInstanceOf(NotFoundException.class);

        verify(itemRequestRepository, times(1)).findById(1L);
        verify(itemService, never()).getItemsByRequestIds(anyList());
    }

}