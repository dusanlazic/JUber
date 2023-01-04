package com.nwt.juber.repository;

import com.nwt.juber.model.Admin;
import com.nwt.juber.model.ChatConversation;
import com.nwt.juber.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, UUID> {

    Optional<ChatConversation> findByUserAndIsArchivedIsFalse(User user);

    Optional<ChatConversation> findByUserAndSupportAndIsArchivedIsFalse(User user, Admin support);

    List<ChatConversation> findBySupportAndIsArchivedIsFalse(Admin support);
}
