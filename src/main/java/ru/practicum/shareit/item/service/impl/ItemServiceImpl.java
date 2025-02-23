package ru.practicum.shareit.item.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.exemption.AuthorizationException;
import ru.practicum.shareit.exceptions.exemption.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDtoResponse add(Long userId, ItemDtoRequestCreate itemDtoRequestCreate) {
        User user = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItemCreate(itemDtoRequestCreate, user);
        return itemMapper.toItemDtoResponse(itemRepository.save(item));
    }

    @Override
    public ItemDtoResponse update(Long userId, Long itemId, ItemDtoRequestUpdate itemDtoRequestUpdate) {
        User user = userMapper.toUser(userService.getById(userId));
        Item item = itemMapper.toItemUpdate(itemDtoRequestUpdate, user, itemId);

        Item itemFromBd = getById(item.getId());
        if (!item.getOwner().getId()
                .equals(itemFromBd.getOwner().getId())) {
            throw new AuthorizationException("Authorization failed");
        }
        if (item.getName() != null) {
            itemFromBd.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemFromBd.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemFromBd.setAvailable(item.getAvailable());
        }
        return itemMapper.toItemDtoResponse(itemRepository.save(item));
    }

    @Override
    public Collection<ItemDtoResponseBooking> get(Long userId) {
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        List<Booking> bookings = new ArrayList<>(bookingRepository.findAllByItem_Owner_IdOrderByStartDateDesc(userId));
        List<Comment> comments = commentRepository.findAllByItem_Owner_Id(userId);

        Map<Long, List<Booking>> bookingsByItemId = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));
        Map<Long, List<Comment>> commentsByItemId = comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();
        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItemId.getOrDefault(item.getId(), List.of());
                    LocalDateTime nextBooking = itemBookings.stream()
                            .map(Booking::getStartDate)
                            .filter(date -> date.isAfter(now))
                            .min(LocalDateTime::compareTo)
                            .orElse(null);
                    LocalDateTime lastBooking = itemBookings.stream()
                            .map(Booking::getStartDate)
                            .filter(date -> date.isBefore(now))
                            .max(LocalDateTime::compareTo)
                            .orElse(null);
                    List<CommentDtoResponseItem> itemComments = commentsByItemId.getOrDefault(item.getId(), List.of())
                            .stream()
                            .map(commentMapper::toItemCommentDtoResponse)
                            .toList();
                    return itemMapper.toItemDtoResponseBooking(item, itemComments, nextBooking, lastBooking);
                }).toList();
    }

    @Override
    public ItemDtoResponseComment getItemWithCommentsById(Long itemId) {
        Item item = getById(itemId);
        List<Comment> comments = commentRepository.findAllByItem_Id(itemId);
        List<CommentDtoResponseItem> itemComments = comments.stream().map(commentMapper::toItemCommentDtoResponse).toList();
        return itemMapper.toItemDtoResponseComment(item, itemComments);
    }

    @Override
    public List<ItemDtoResponse> getByString(String query) {
        return itemRepository.findItemsByNameLikeIgnoreCaseAndAvailableTrue(query).stream().map(itemMapper::toItemDtoResponse).toList();
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", itemId)));
    }

    @Override
    public CommentDtoResponse addComment(CommentDtoRequestCreate commentDtoRequestCreate, Long itemId, Long authorId) {
        Item item = getById(itemId);
        User author = userMapper.toUser(userService.getById(authorId));

        if (bookingRepository.findPastByItem_IdAndBooker_Id(itemId, authorId).isEmpty()) {
            throw new NotFoundException(String.format("Item id=%d completed booking of user id=%d not found", itemId, authorId));
        }

        Comment comment = commentMapper.toCommentCreate(commentDtoRequestCreate, item, author);
        comment.setCreationDate(LocalDateTime.now());
        return commentMapper.toCommentDtoResponse(
                commentRepository.save(comment),
                itemMapper.toItemDtoResponse(item),
                author.getName());
    }

}
