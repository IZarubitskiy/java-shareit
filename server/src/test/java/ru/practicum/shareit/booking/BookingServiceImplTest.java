package ru.practicum.shareit.booking;


import jakarta.validation.ValidationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequestCreate;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Spy
    private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Spy
    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final User user;
    private final UserDtoResponse userDtoResponse;
    private final Item item;
    private final ItemDtoResponse itemDtoResponse;
    private final Booking booking;
    private final BookingDtoRequestCreate bookingDtoRequestCreate;
    private final BookingDtoResponse bookingDtoResponse;

    public BookingServiceImplTest() {
        user = new User();
        user.setId(1L);
        user.setName("user name test");
        user.setEmail("user@test.com");

        userDtoResponse = UserDtoResponse.builder()
                .id(1L)
                .name("user name test")
                .email("user@test.com")
                .build();

        item = new Item();
        item.setId(1L);
        item.setName("item name test");
        item.setDescription("item description test");
        item.setAvailable(true);
        item.setOwner(user);

        itemDtoResponse = ItemDtoResponse.builder()
                .id(1L)
                .name("item name test")
                .description("item description test")
                .available(true)
                .ownerId(1L)
                .build();

        booking = new Booking();
        booking.setId(1L);
        booking.setStartDate(LocalDateTime.now().plusDays(1));
        booking.setEndDate(LocalDateTime.now().plusDays(2));
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(StatusBooking.WAITING);

        bookingDtoRequestCreate = BookingDtoRequestCreate.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(StatusBooking.WAITING)
                .item(itemDtoResponse)
                .booker(userDtoResponse)
                .build();
    }

    @Test
    void shouldCreateBooking() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemService.getById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse actualResponse = bookingService.createBooking(bookingDtoRequestCreate, 1L);


        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("start", "end")
                .isEqualTo(bookingDtoResponse);

        verify(userService, times(1)).getById(1L);
        verify(itemService, times(1)).getById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBookerNotFoundForCreateBooking() {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException(""));

        assertThatThrownBy(() -> bookingService.createBooking(bookingDtoRequestCreate, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemService, never()).getById(anyLong());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFoundForCreateBooking() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemService.getById(anyLong())).thenThrow(new NotFoundException(""));

        assertThatThrownBy(() -> bookingService.createBooking(bookingDtoRequestCreate, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemService, times(1)).getById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenItemNotAvailable() {
        Item naItem = new Item();
        naItem.setId(1L);
        naItem.setName("item name test");
        naItem.setDescription("item description test");
        naItem.setAvailable(false);
        naItem.setOwner(user);

        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemService.getById(anyLong())).thenReturn(naItem);

        assertThatThrownBy(() -> bookingService.createBooking(bookingDtoRequestCreate, 1L))
                .isInstanceOf(ValidationException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemService, times(1)).getById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldSetApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse expectedResponse = BookingDtoResponse.builder()
                .id(1L)
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(StatusBooking.APPROVED)
                .item(itemDtoResponse)
                .booker(userDtoResponse)
                .build();

        BookingDtoResponse actualResponse = bookingService.setApproved(1L, true, 1L);

        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("start", "end")
                .isEqualTo(expectedResponse);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void shouldSetRejected() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDtoResponse expectedResponse = BookingDtoResponse.builder()
                .id(1L)
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(StatusBooking.REJECTED)
                .item(itemDtoResponse)
                .booker(userDtoResponse)
                .build();

        BookingDtoResponse actualResponse = bookingService.setApproved(1L, false, 1L);

        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("start", "end")
                .isEqualTo(expectedResponse);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUserIsNotOwnerForSetApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.setApproved(1L, true, 2L))
                .isInstanceOf(AuthorizationException.class);

        verify(bookingRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void shouldGetBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDtoResponse actualResponse = bookingService.getBooking(1L, 1L);

        Assertions.assertThat(actualResponse)
                .usingRecursiveComparison()
                .ignoringFields("start", "end")
                .isEqualTo(bookingDtoResponse);

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowAuthorizationExceptionWhenUserIsNotBookerOrOwnerForGetBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.getBooking(1L, 2L))
                .isInstanceOf(AuthorizationException.class);

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenBookingNotFoundForGetBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.getBooking(1L, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(bookingRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetBookerBookingsAll() {
        when(bookingRepository.findAllByBooker_IdOrderByStartDateDesc(anyLong())).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.ALL);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByBooker_IdOrderByStartDateDesc(1L);
    }

    @Test
    void shouldGetBookerBookingsCurrent() {
        when(bookingRepository.findByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.CURRENT);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByBooker_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        eq(1L), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void shouldGetBookerBookingsPast() {
        when(bookingRepository.findByBooker_IdAndEndDateBeforeOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.PAST);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByBooker_IdAndEndDateBeforeOrderByStartDateDesc(eq(1L), any(LocalDateTime.class));
    }

    @Test
    void shouldGetBookerBookingsFuture() {
        when(bookingRepository.findByBooker_IdAndStartDateAfterOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.FUTURE);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByBooker_IdAndStartDateAfterOrderByStartDateDesc(eq(1L), any(LocalDateTime.class));
    }

    @Test
    void shouldGetBookerBookingsWaiting() {
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDateDesc(anyLong(), eq(StatusBooking.WAITING))).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.WAITING);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByBooker_IdAndStatusOrderByStartDateDesc(1L, StatusBooking.WAITING);
    }

    @Test
    void shouldGetBookerBookingsRejected() {
        when(bookingRepository.findAllByBooker_IdAndStatusOrderByStartDateDesc(anyLong(), eq(StatusBooking.REJECTED))).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getBookerBookings(1L, State.REJECTED);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByBooker_IdAndStatusOrderByStartDateDesc(1L, StatusBooking.REJECTED);
    }

    @Test
    void shouldGetOwnerBookingsAll() {
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(anyLong())).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.ALL);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByItem_Owner_IdOrderByStartDateDesc(1L);
    }

    @Test
    void shouldGetOwnerBookingsCurrent() {
        when(bookingRepository.findByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.CURRENT);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByItem_Owner_IdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(
                        eq(1L),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }

    @Test
    void shouldGetOwnerBookingsPast() {
        when(bookingRepository.findByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.PAST);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByItem_Owner_IdAndEndDateBeforeOrderByStartDateDesc(eq(1L), any(LocalDateTime.class));
    }

    @Test
    void shouldGetOwnerBookingsFuture() {
        when(bookingRepository.findByItem_Owner_IdAndStartDateAfterOrderByStartDateDesc(
                anyLong(),
                any(LocalDateTime.class))
        ).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.FUTURE);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1))
                .findByItem_Owner_IdAndStartDateAfterOrderByStartDateDesc(eq(1L), any(LocalDateTime.class));
    }

    @Test
    void shouldGetOwnerBookingsWaiting() {
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(anyLong(), eq(StatusBooking.WAITING))).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.WAITING);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(1L, StatusBooking.WAITING);
    }

    @Test
    void shouldGetOwnerBookingsRejected() {
        when(bookingRepository.findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(anyLong(), eq(StatusBooking.REJECTED))).thenReturn(List.of(booking));

        Collection<BookingDtoResponse> actualResponses = bookingService.getOwnerBookings(1L, State.REJECTED);

        Assertions.assertThat(actualResponses)
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("start", "end")
                .containsExactlyInAnyOrder(bookingDtoResponse);

        verify(bookingRepository, times(1)).findAllByItem_Owner_IdAndStatusOrderByStartDateDesc(1L, StatusBooking.REJECTED);
    }

    @Test
    void shouldThrowValidationExceptionWhenStartAfterEnd() {
        when(itemService.getById(anyLong())).thenReturn(item);
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);

        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(1))
                .build();

        assertThatThrownBy(() -> bookingService.createBooking(createBookingRequest, 1L))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void shouldThrowValidationExceptionWhenStartEqualEnd() {
        when(itemService.getById(anyLong())).thenReturn(item);
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);

        LocalDateTime now = LocalDateTime.now();

        BookingDtoRequestCreate createBookingRequest = BookingDtoRequestCreate.builder()
                .itemId(1L)
                .start(now)
                .end(now)
                .build();

        assertThatThrownBy(() -> bookingService.createBooking(createBookingRequest, 1L))
                .isInstanceOf(ValidationException.class);
    }
}