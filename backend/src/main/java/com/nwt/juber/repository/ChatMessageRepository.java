package com.nwt.juber.repository;

import com.nwt.juber.model.PersistedChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<PersistedChatMessage, UUID> {
}
