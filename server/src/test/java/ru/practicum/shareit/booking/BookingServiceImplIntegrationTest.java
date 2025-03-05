package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoRequestCreate;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

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
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private BookingRepository bookingRepository;

    private UserDtoResponse userResponse;
    private UserDtoResponse ownerResponse;
    private ItemDtoResponse itemResponse;

    @BeforeEach
    void setUp() {
        UserDtoRequestCreate createUserRequest = UserDtoRequestCreate.builder()
                .name("test user")
                .email("test@example.com")
                .build();
        userResponse = userService.add(createUserRequest);

        UserDtoRequestCreate createOwnerRequest = UserDtoRequestCreate.builder()
                .name("test owner")
                .email("owner@example.com")
                .build();
        ownerResponse = userService.add(createOwnerRequest);

        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(true)
                .build();
        itemResponse = itemService.add(ownerResponse.getId(), createItemRequest);
    }

    @Test
    void shouldCreateBooking() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDtoResponse bookingResponse = bookingService.createBooking(createBookingRequest, userResponse.getId());

        assertThat(bookingResponse.getId()).isNotNull();
        assertThat(bookingResponse.getStatus()).isEqualTo(StatusBooking.WAITING);
        assertThat(bookingResponse.getItem().getId()).isEqualTo(itemResponse.getId());
        assertThat(bookingResponse.getBooker().getId()).isEqualTo(userResponse.getId());

        Booking savedBooking = bookingRepository.findById(bookingResponse.getId()).orElseThrow();
        assertThat(savedBooking.getStatus()).isEqualTo(StatusBooking.WAITING);
        assertThat(savedBooking.getItem().getId()).isEqualTo(itemResponse.getId());
        assertThat(savedBooking.getBooker().getId()).isEqualTo(userResponse.getId());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateBooking() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThatThrownBy(() -> bookingService.createBooking(createBookingRequest, 999L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFoundForCreateBooking() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(999L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThatThrownBy(() -> bookingService.createBooking(createBookingRequest, userResponse.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldThrowValidationExceptionWhenItemNotAvailable() {
        ItemDtoRequestCreate createItemRequest = ItemDtoRequestCreate.builder()
                .name("test item")
                .description("item description")
                .available(false)
                .build();
        ItemDtoResponse unavailableItemResponse = itemService.add(ownerResponse.getId(), createItemRequest);

        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(unavailableItemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        assertThatThrownBy(() -> bookingService.createBooking(createBookingRequest, userResponse.getId()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldSetApproved() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDtoResponse bookingResponse = bookingService.createBooking(createBookingRequest, userResponse.getId());

        BookingDtoResponse approvedBookingResponse = bookingService.setApproved(bookingResponse.getId(), true, ownerResponse.getId());

        assertThat(approvedBookingResponse.getStatus()).isEqualTo(StatusBooking.APPROVED);

        Booking savedBooking = bookingRepository.findById(bookingResponse.getId()).orElseThrow();
        assertThat(savedBooking.getStatus()).isEqualTo(StatusBooking.APPROVED);
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUserIsNotOwnerForSetApproved() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDtoResponse bookingResponse = bookingService.createBooking(createBookingRequest, userResponse.getId());

        assertThatThrownBy(() -> bookingService.setApproved(bookingResponse.getId(), true, userResponse.getId()))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    void shouldGetBooking() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDtoResponse bookingResponse = bookingService.createBooking(createBookingRequest, userResponse.getId());

        BookingDtoResponse foundBooking = bookingService.getBooking(bookingResponse.getId(), userResponse.getId());

        assertThat(foundBooking.getId()).isEqualTo(bookingResponse.getId());
        assertThat(foundBooking.getStatus()).isEqualTo(StatusBooking.WAITING);
        assertThat(foundBooking.getItem().getId()).isEqualTo(itemResponse.getId());
        assertThat(foundBooking.getBooker().getId()).isEqualTo(userResponse.getId());
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUserIsNotBookerOrOwnerForGetBooking() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        BookingDtoResponse bookingResponse = bookingService.createBooking(createBookingRequest, userResponse.getId());

        assertThatThrownBy(() -> bookingService.getBooking(bookingResponse.getId(), 999L))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    void shouldGetBookerBookings() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        bookingService.createBooking(createBookingRequest, userResponse.getId());

        Collection<BookingDtoResponse> bookings = bookingService.getBookerBookings(userResponse.getId(), State.ALL);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.iterator().next().getItem().getId()).isEqualTo(itemResponse.getId());
        assertThat(bookings.iterator().next().getBooker().getId()).isEqualTo(userResponse.getId());
    }

    @Test
    void shouldGetOwnerBookings() {
        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(itemResponse.getId())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        bookingService.createBooking(createBookingRequest, userResponse.getId());

        Collection<BookingDtoResponse> bookings = bookingService.getOwnerBookings(ownerResponse.getId(), State.ALL);

        assertThat(bookings).hasSize(1);
        assertThat(bookings.iterator().next().getItem().getId()).isEqualTo(itemResponse.getId());
        assertThat(bookings.iterator().next().getBooker().getId()).isEqualTo(userResponse.getId());
    }
}