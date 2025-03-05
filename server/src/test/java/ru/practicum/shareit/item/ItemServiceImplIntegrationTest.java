package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
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
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingServiceImpl bookingServiceImpl;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private UserDtoResponse userDtoResponse;
    private ItemDtoResponse itemDtoResponse;

    @BeforeEach
    void setUp() {
        UserDtoRequestCreate createUserRequest = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test@example.com")
                .build();
        userDtoResponse = userService.add(createUserRequest);

        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .build();
        itemDtoResponse = itemService.add(userDtoResponse.getId(), createItemRequest);
    }

    @Test
    void shouldCreateItem() {

        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("new item")
                .description("new item description")
                .available(true)
                .build();

        ItemDtoResponse newItemResponse = itemService.add(userDtoResponse.getId(), createItemRequest);

        assertThat(newItemResponse.getId()).isNotNull();
        assertThat(newItemResponse.getName()).isEqualTo("new item");
        assertThat(newItemResponse.getDescription()).isEqualTo("new item description");
        assertThat(newItemResponse.getAvailable()).isTrue();

        Item savedItem = itemRepository.findById(newItemResponse.getId()).orElseThrow();
        assertThat(savedItem.getName()).isEqualTo("new item");
        assertThat(savedItem.getDescription()).isEqualTo("new item description");
        assertThat(savedItem.getAvailable()).isTrue();
    }

    /*
                @Test
                void shouldCreateItemWithRequest() {

                    UserDtoRequestCreate createRequester = UserDtoRequestCreate.builder()
                            .name("test user requester")
                            .email("test-requester@example.com")
                            .build();
                    userDtoResponse = userService.add(createRequester);
                    System.out.println(userDtoResponse);

                    ItemRequestDtoRequestCreate itemRequestDtoRequestCreate = ItemRequestDtoRequestCreate.builder()
                            .description("test")
                            .build();
                    System.out.println(itemRequestDtoRequestCreate);
                    ItemRequestDtoResponse itemRequestDtoResponse = itemRequestService.add(itemRequestDtoRequestCreate, userDtoResponse.getId());
                    System.out.println(itemRequestDtoResponse);
                    System.out.println(itemRequestService.getAll());
                    ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                            .name("new item")
                            .description("new item description")
                            .available(false)
                            .requestId(itemRequestDtoResponse.getId())
                            .build();

                    ItemDtoResponse newItemResponse = itemService.add(userDtoResponse.getId(), createItemRequest);
                    System.out.println(newItemResponse);

                    System.out.println(itemRequestService.getById(newItemResponse.getRequestId()));
                    System.out.println(itemRequestRepository.findById(newItemResponse.getRequestId()));
                    assertThat(newItemResponse.getId()).isNotNull();
                    assertThat(newItemResponse.getName()).isEqualTo("new item");
                    assertThat(newItemResponse.getDescription()).isEqualTo("new item description");
                    assertThat(newItemResponse.getAvailable()).isFalse();
                    assertThat(newItemResponse.getRequestId()).isEqualTo(itemRequestDtoResponse.getId());

                    Item savedItem = itemRepository.findById(newItemResponse.getId()).orElseThrow();
                    assertThat(savedItem.getName()).isEqualTo("new item");
                    assertThat(savedItem.getDescription()).isEqualTo("new item description");
                    assertThat(savedItem.getAvailable()).isFalse();
                    assertThat(savedItem.getItemRequest().getId()).isEqualTo(itemRequestDtoResponse.getId());

                }
    */
    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateItem() {
        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("new item")
                .description("new item description")
                .available(true)
                .build();

        assertThatThrownBy(() -> itemService.add(999L, createItemRequest))
                .isInstanceOf(NotFoundException.class);
    }


    @Test
    void shouldUpdateItem() {
        ItemDtoRequestUpdate updateItemRequest = ItemDtoRequestUpdate.builder()
                .name("updated item")
                .description("updated item description")
                .available(false)
                .build();

        ItemDtoResponse updatedItemResponse = itemService.update(userDtoResponse.getId(), itemDtoResponse.getId(), updateItemRequest);
        System.out.println(updatedItemResponse);

        assertThat(updatedItemResponse.getId()).isEqualTo(itemDtoResponse.getId());
        assertThat(updatedItemResponse.getName()).isEqualTo("updated item");
        assertThat(updatedItemResponse.getDescription()).isEqualTo("updated item description");
        assertThat(updatedItemResponse.getAvailable()).isFalse();
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUpdatingItemWithWrongOwner() {
        UserDtoRequestCreate createUserRequest = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test2@example.com")
                .build();
        userDtoResponse = userService.add(createUserRequest);

        ItemDtoRequestUpdate updateItemRequest = ItemDtoRequestUpdate.builder()
                .name("updated item")
                .description("updated item description")
                .available(false)
                .build();

        assertThatThrownBy(() -> itemService.update(userDtoResponse.getId(), itemDtoResponse.getId(), updateItemRequest))
                .isInstanceOf(AuthorizationException.class);
    }


    @Test
    void shouldFindItemById() {
        Item foundItem = itemService.getById(itemDtoResponse.getId());

        assertThat(foundItem.getId()).isEqualTo(itemDtoResponse.getId());
        assertThat(foundItem.getName()).isEqualTo("test item");
        assertThat(foundItem.getDescription()).isEqualTo("item description");
        assertThat(foundItem.getAvailable()).isTrue();
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFound() {
        assertThatThrownBy(() -> itemService.getById(999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldFindAllUserItems() {
        List<ItemDtoResponseSeek> userItems = itemService.get(userDtoResponse.getId());

        assertThat(userItems).hasSize(1);
        assertThat(userItems.getFirst().getName()).isEqualTo("test item");
        assertThat(userItems.getFirst().getDescription()).isEqualTo("item description");
        assertThat(userItems.getFirst().getAvailable()).isTrue();
    }

    @Test
    void shouldSearchItems() {
        List<ItemDtoResponse> foundItems = itemService.getByString("test");
        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.getFirst().getName()).isEqualTo("test item");
        assertThat(foundItems.getFirst().getDescription()).isEqualTo("item description");
        assertThat(foundItems.getFirst().getAvailable()).isTrue();
    }

    @Test
    void shouldAddComment() {
        CommentDtoRequestCreate createCommentRequest = CommentDtoRequestCreate.builder()
                .text("test comment")
                .build();

        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemDtoResponse.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(1))
                .build();

        bookingServiceImpl.createBooking(createBookingRequest, userDtoResponse.getId());

        CommentDtoResponse commentResponse = itemService.addComment(createCommentRequest, itemDtoResponse.getId(), userDtoResponse.getId());

        assertThat(commentResponse.getText()).isEqualTo("test comment");
        assertThat(commentResponse.getAuthorName()).isEqualTo("test user");
        assertThat(commentResponse.getItem().getName()).isEqualTo("test item");
    }

    @Test
    void shouldThrowValidationExceptionWhenAddingCommentWithoutBooking() {
        CommentDtoRequestCreate createCommentRequest = CommentDtoRequestCreate.builder()
                .text("test comment")
                .build();

        assertThatThrownBy(() -> itemService.addComment(createCommentRequest, itemDtoResponse.getId(), userDtoResponse.getId()))
                .isInstanceOf(ValidationException.class);
    }

}