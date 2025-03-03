package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestCreate;
import ru.practicum.shareit.item.dto.ItemDtoRequestUpdate;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(ItemDtoRequestCreate itemDtoRequestCreate, Long ownerId) {
        return post("", ownerId, itemDtoRequestCreate);
    }

    public ResponseEntity<Object> updateItem(ItemDtoRequestUpdate itemDtoRequestUpdate, Long ownerId, Long itemId) {
        return patch("/" + itemId, ownerId, itemDtoRequestUpdate);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long ownerId) {
        return get("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> getAllUserItems(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> searchItems(String text, Long ownerId) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", ownerId, parameters);
    }

    public ResponseEntity<Object> addComment(CommentDtoRequestCreate commentDtoRequestCreate, Long authorId, Long itemId) {
        return post("/" + itemId + "/comment", authorId, commentDtoRequestCreate);
    }
}
