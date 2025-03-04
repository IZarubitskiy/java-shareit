package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserService userService;

    @Spy
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Spy
    private CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final User user;
    private final UserDtoResponse userDtoResponse;
    private final Item item;
    private final ItemDtoRequestCreate itemDtoRequestCreate;
    private final ItemDtoRequestUpdate itemDtoRequestUpdate;
    private final Comment comment;
    private final CommentDtoRequestCreate createCommentRequest;

    public ItemServiceImplTest() {
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

        itemDtoRequestCreate = ItemDtoRequestCreate.builder()
                .name("item name test")
                .description("item description test")
                .available(true)
                .requestId(null)
                .build();

        itemDtoRequestUpdate = ItemDtoRequestUpdate.builder()
                .name("updated item name test")
                .description("updated item description test")
                .available(false)
                .build();

        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment text test");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreationDate(LocalDateTime.now());

        createCommentRequest = CommentDtoRequestCreate.builder()
                .text("comment text test")
                .build();
    }

    @Test
    void shouldCreateItem() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDtoResponse expectedResponse = itemMapper.toItemDtoResponse(item);
        ItemDtoResponse actualResponse = itemService.add(1L, itemDtoRequestCreate);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(userService, times(1)).getById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void shouldCreateItemWithRequest() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("request description test");
        request.setRequester(user);
        request.setCreationDate(LocalDateTime.now());

        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDtoRequestCreate createItemRequestWithRequest = ItemDtoRequestCreate.builder()
                .name("item name test")
                .description("item description test")
                .available(true)
                .requestId(1L)
                .build();

        ItemDtoResponse expectedResponse = itemMapper.toItemDtoResponse(item);
        ItemDtoResponse actualResponse = itemService.add(1L, createItemRequestWithRequest);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(userService, times(1)).getById(1L);
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRequestNotFoundForCreateItem() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());

        ItemDtoRequestCreate createItemRequestWithRequest = ItemDtoRequestCreate.builder()
                .name("item name test")
                .description("item description test")
                .available(true)
                .requestId(1L)
                .build();

        assertThatThrownBy(() -> itemService.add(1L, createItemRequestWithRequest))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemRequestRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundForCreateItem() {
        when(userService.getById(anyLong())).thenThrow(new NotFoundException(""));

        assertThatThrownBy(() -> itemService.add(1L, itemDtoRequestCreate))
                .isInstanceOf(NotFoundException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void shouldUpdateItem() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> {
            Item savedItem = invocation.getArgument(0);
            savedItem.setName(itemDtoRequestUpdate.getName());
            savedItem.setDescription(itemDtoRequestUpdate.getDescription());
            savedItem.setAvailable(itemDtoRequestUpdate.getAvailable());
            return savedItem;
        });

        ItemDtoResponse actualResponse = itemService.update(1L, 1L, itemDtoRequestUpdate);

        ItemDtoResponse expectedResponse = ItemDtoResponse.builder()
                .id(1L)
                .name("updated item name test")
                .description("updated item description test")
                .available(false)
                .ownerId(1L)
                .requestId(null)
                .build();

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFoundForUpdateItem() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.update(1L, 1L, itemDtoRequestUpdate))
                .isInstanceOf(NotFoundException.class);

        verify(itemRepository, times(1)).findById(1L);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void shouldFindItem() {
        // when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDtoResponse expectedResponse = itemMapper.toItemDtoResponse(item);
        Item actualResponse = itemService.getById(1L);

        assertThat(actualResponse).isEqualTo(item);

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFound() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getById(1L))
                .isInstanceOf(NotFoundException.class);

        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindItemWithComments() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItem_Id(anyLong())).thenReturn(of(comment));

        ItemDtoResponseSeek expectedResponse = itemMapper.toItemDtoResponseSeek(item, of(commentMapper.toItemCommentDtoResponse(comment)), null, null);
        ItemDtoResponseSeek actualResponse = itemService.getItemWithCommentsById(1L);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(itemRepository, times(1)).findById(1L);
        verify(commentRepository, times(1)).findAllByItem_Id(1L);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFoundForFindItemWithComments() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemWithCommentsById(1L))
                .isInstanceOf(NotFoundException.class);

        verify(itemRepository, times(1)).findById(1L);
        verify(commentRepository, never()).findAllByItem_Id(anyLong());
    }
/*
    @Test
    void shouldGetAllUserItems() {
        when(itemRepository.findItemsByOwnerId(anyLong())).thenReturn(of(item));
        when(bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findAllByItem_Owner_Id(anyLong())).thenReturn(Collections.emptyList());

        List<ItemDtoResponseSeek> expectedResponses = of(
                itemMapper.toItemDtoResponseSeek(item, Collections.emptyList(), null, null)
        );
        List<ItemDtoResponseSeek> actualResponses = itemService.get(1L);

        assertThat(actualResponses).isEqualTo(expectedResponses);

        verify(itemRepository, times(1)).findItemsByOwnerId(1L);
        verify(bookingRepository, times(1)).findAllByItem_Owner_IdOrderByStartDateDesc(1L);
        verify(commentRepository, times(1)).findAllByItem_Owner_Id(1L);
    }

    @Test
    void shouldSearchItems() {
        when(itemRepository.findItemsByNameLikeIgnoreCaseAndAvailableTrue(anyString())).thenReturn(of(item));

        List<ItemDtoResponse> expectedResponses = of(itemMapper.toItemDtoResponse(item));
        List<ItemDtoResponse> actualResponses = itemService.getByString("item");

        assertThat(actualResponses).isEqualTo(expectedResponses);

        verify(itemRepository, times(1)).findItemsByNameLikeIgnoreCaseAndAvailableTrue("item");
    }
/*
    @Test
    void shouldAddComment() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findPastByItem_IdAndBooker_Id(anyLong(), anyLong())).thenReturn(List.of(new Booking()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDtoResponse expectedResponse = commentMapper.toCommentDtoResponse(
                comment, itemMapper.toItemDtoResponse(item), user.getName()
        );
        CommentDtoResponse actualResponse = itemService.addComment(createCommentRequest, 1L, 1L);

        assertThat(actualResponse).isEqualTo(expectedResponse);

        verify(userService, times(1)).getById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findPastByItem_IdAndBooker_Id(1L, 1L);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenItemNotFoundForAddComment() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.addComment(createCommentRequest, 1L, 1L))
                .isInstanceOf(NotFoundException.class);

        verify(userService, never()).getById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).findPastByItem_IdAndBooker_Id(anyLong(), anyLong());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenAddingCommentWithoutBooking() {
        when(userService.getById(anyLong())).thenReturn(userDtoResponse);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findPastByItem_IdAndBooker_Id(anyLong(), anyLong())).thenReturn(of());

        assertThatThrownBy(() -> itemService.addComment(createCommentRequest, 1L, 1L))
                .isInstanceOf(ValidationException.class);

        verify(userService, times(1)).getById(1L);
        verify(itemRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).findPastByItem_IdAndBooker_Id(1L, 1L);
    }

    @Test
    void shouldFindItemsByRequestIds() {
        when(itemRepository.findItemsByItemRequest_IdIn(anyList())).thenReturn(of(item));

        List<ItemDtoResponse> expectedResponses = of(itemMapper.toItemDtoResponse(item));
        List<ItemDtoResponse> actualResponses = itemService.getItemsByRequestIds(of(1L));

        assertThat(actualResponses).isEqualTo(expectedResponses);

        verify(itemRepository, times(1)).findItemsByItemRequest_IdIn(of(1L));
    }
    */

}