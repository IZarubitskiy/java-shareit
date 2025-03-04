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
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

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

    private UserDtoResponse userResponse;
    private ItemDtoResponseSeek itemResponse;

    @BeforeEach
    void setUp() {
        UserDtoRequestCreate createUserRequest = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test@example.com")
                .build();
        userResponse = userService.add(createUserRequest);

        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .build();
        //   itemResponse = itemService.add(userResponse.getId(), createItemRequest);
    }

    @Test
    void shouldCreateItem() {
        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("new item")
                .description("new item description")
                .available(true)
                .build();

        ItemDtoResponse newItemResponse = itemService.add(userResponse.getId(), createItemRequest);

        assertThat(newItemResponse.getId()).isNotNull();
        assertThat(newItemResponse.getName()).isEqualTo("new item");
        assertThat(newItemResponse.getDescription()).isEqualTo("new item description");
        assertThat(newItemResponse.getAvailable()).isTrue();

        Item savedItem = itemRepository.findById(newItemResponse.getId()).orElseThrow();
        assertThat(savedItem.getName()).isEqualTo("new item");
        assertThat(savedItem.getDescription()).isEqualTo("new item description");
        assertThat(savedItem.getAvailable()).isTrue();
    }

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

    /*
        @Test
        void shouldUpdateItem() {
            ItemDtoRequestUpdate updateItemRequest = ItemDtoRequestUpdate.builder()
                    .name("updated item")
                    .description("updated item description")
                    .available(false)
                    .build();

            ItemDtoResponse updatedItemResponse = itemService.update(itemResponse.getId(), updateItemRequest, userResponse.getId());

            assertThat(updatedItemResponse.getId()).isEqualTo(itemResponse.getId());
            assertThat(updatedItemResponse.getName()).isEqualTo("updated item");
            assertThat(updatedItemResponse.getDescription()).isEqualTo("updated item description");
            assertThat(updatedItemResponse.getAvailable()).isFalse();
        }*/
/*
    @Test
    void shouldThrowAuthorizationExceptionWhenUpdatingItemWithWrongOwner() {
        UserDtoRequestCreate createUserRequest = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test2@example.com")
                .build();
        userResponse = userService.add(createUserRequest);

        ItemDtoRequestUpdate updateItemRequest = ItemDtoRequestUpdate.builder()
                .name("updated item")
                .description("updated item description")
                .available(false)
                .build();

        assertThatThrownBy(() -> itemService.update(itemResponse.getId(), updateItemRequest, userResponse.getId()))
                .isInstanceOf(AuthorizationException.class);
    }


    @Test
    void shouldFindItemById() {
        ItemDtoResponse foundItem = itemService.get(itemResponse.getId());

        assertThat(foundItem.getId()).isEqualTo(itemResponse.getId());
        assertThat(foundItem.getName()).isEqualTo("test item");
        assertThat(foundItem.getDescription()).isEqualTo("item description");
        assertThat(foundItem.getAvailable()).isTrue();
    }
*/
    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFound() {
        assertThatThrownBy(() -> itemService.getById(999L))
                .isInstanceOf(NotFoundException.class);
    }
/*
    @Test
    void shouldFindAllUserItems() {
        List<ItemDtoResponseSeek> userItems = itemService.get(userResponse.getId());

        assertThat(userItems).hasSize(1);
        assertThat(userItems.get(0).getName()).isEqualTo("test item");
        assertThat(userItems.get(0).getDescription()).isEqualTo("item description");
        assertThat(userItems.get(0).getAvailable()).isTrue();
    }

    @Test
    void shouldSearchItems() {
        List<ItemDtoResponse> foundItems = itemService.getByString("test");

        assertThat(foundItems).hasSize(1);
        assertThat(foundItems.get(0).getName()).isEqualTo("test item");
        assertThat(foundItems.get(0).getDescription()).isEqualTo("item description");
        assertThat(foundItems.get(0).getAvailable()).isTrue();
    }*/

    @Test
    void shouldAddComment() {
        CommentDtoRequestCreate createCommentRequest = CommentDtoRequestCreate.builder()
                .text("test comment")
                .build();

        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        bookingServiceImpl.createBooking(createBookingRequest, userResponse.getId());

        CommentDtoResponse commentResponse = itemService.addComment(createCommentRequest, itemResponse.getId(), userResponse.getId());

        assertThat(commentResponse.getText()).isEqualTo("test comment");
        assertThat(commentResponse.getAuthorName()).isEqualTo("test user");
        assertThat(commentResponse.getItem().getName()).isEqualTo("test item");
    }

    @Test
    void shouldThrowValidationExceptionWhenAddingCommentWithoutBooking() {
        CommentDtoRequestCreate createCommentRequest = CommentDtoRequestCreate.builder()
                .text("test comment")
                .build();

        assertThatThrownBy(() -> itemService.addComment(createCommentRequest, itemResponse.getId(), userResponse.getId()))
                .isInstanceOf(ValidationException.class);
    }
}