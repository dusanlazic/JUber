package com.nwt.juber.repository;

import com.nwt.juber.model.PersistedChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<PersistedChatMessage, UUID> {
}
