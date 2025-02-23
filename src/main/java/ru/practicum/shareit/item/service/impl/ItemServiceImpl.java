package ru.practicum.shareit.item.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
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
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDtoResponse add(Long userId, ItemDtoCreateRequest itemDtoCreateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = itemMapper.toItemCreate(itemDtoCreateRequest, user);
        return itemMapper.toItemDtoResponse(itemRepository.save(item));
    }

    @Override
    public ItemDtoResponse update(Long userId, Long itemId, ItemDtoUpdateRequest itemDtoUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", userId)));
        Item item = itemMapper.toItemUpdate(itemDtoUpdateRequest, user, itemId);

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
    public Collection<ItemWithCommentsDtoResponse> get(Long userId) {
        return itemRepository.get(userId).stream().map(itemMapper::toItemDtoResponse).toList();
    }

    @Override
    public ItemWithCommentsDtoResponse getItemWithCommentsById(Long itemId) {
        Item item = getById(itemId);
        List<Comment> comments = commentRepository.findAllByItem_Id(itemId);
        List<CommentItemDtoResponse> itemComments = comments.stream().map(commentMapper::toItemCommentDtoResponse).toList();
        return itemMapper.toItemWithCommentsDtoRespoonse(itemMapper.toItemDtoResponse(item), itemComments);

    }

    @Override
    public List<ItemDtoResponse> getByString(String query) {
        return itemRepository.findItemsByNameLikeIgnoreCaseAndAvailableTrue(query).stream().map(itemMapper::toItemDtoResponse).toList();
    }

    @Override
    public Item getById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", itemId))));
    }

    @Override
    public CommentDtoCreateResponse addComment(CommentDtoCreateRequest commentDtoCreateRequest, Long itemId, Long authorId) {
        Item item = getItem(itemId);
        User author = userMapper.responseToUser(userService.getUser(authorId));

        if (bookingRepository.findPastByItem_IdAndBooker_Id(itemId, authorId).isEmpty()) {
            throw new ValidationException(String.format("Item id=%d completed booking of user id=%d not found", itemId, authorId));
        }

        Comment comment = commentMapper.createRequestToComment(createCommentRequest, item, author);
        comment.setCreationDate(LocalDateTime.now());
        return commentMapper.commentToMergeResponse(
                commentRepository.save(comment),
                itemMapper.itemToResponse(item),
                author.getName());
    }

}
