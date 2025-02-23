package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.CommentDtoResponseItem;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommentMapper {

    @Mapping(target = "item", source = "item")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "id", ignore = true)
    Comment toCommentCreate(CommentDtoRequestCreate createCommentRequest, Item item, User author);

    @Mapping(target = "item", source = "item")
    @Mapping(target = "authorName", source = "authorName")
    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "created", source = "comment.creationDate")
    CommentDtoResponse toCommentDtoResponse(Comment comment, ItemDtoResponse item, String authorName);

    @Mapping(target = "authorName", source = "comment.author.name")
    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "created", source = "comment.creationDate")
    CommentDtoResponseItem toItemCommentDtoResponse(Comment comment);
}