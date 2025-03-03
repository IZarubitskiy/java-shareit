package ru.practicum.shareit.request.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findRequestsByRequester_IdOrderByCreationDateDesc(Long requesterId);

    @Query("SELECT r FROM Request r ORDER BY r.creationDate DESC")
    List<ItemRequest> findAllOrderByCreationDateDesc();
}