package com.nwt.juber.service;

import com.nwt.juber.dto.request.ChatMessageRequest;
import com.nwt.juber.dto.response.ChatConversationResponse;
import com.nwt.juber.dto.response.ChatMessageResponse;
import com.nwt.juber.exception.ConversationNotFoundException;
import com.nwt.juber.exception.UserNotFoundException;
import com.nwt.juber.model.Admin;
import com.nwt.juber.model.ChatConversation;
import com.nwt.juber.model.PersistedChatMessage;
import com.nwt.juber.model.User;
import com.nwt.juber.repository.AdminRepository;
import com.nwt.juber.repository.ChatConversationRepository;
import com.nwt.juber.repository.ChatMessageRepository;
import com.nwt.juber.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    private ChatConversationRepository conversationRepository;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    public List<ChatMessageResponse> getMessages(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository.findByUserAndIsArchivedIsFalse(user).orElseThrow(ConversationNotFoundException::new);
        List<PersistedChatMessage> messages = conversation.getMessages();

        return messages.stream()
                .map(m -> new ChatMessageResponse(m.getContent(), m.getSentAt(), m.getIsFromSupport()))
                .sorted(Comparator.comparing(ChatMessageResponse::getSentAt).reversed())
                .toList();
    }

    public List<ChatMessageResponse> getMessages(Authentication authentication, UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Admin support = (Admin) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository
                .findByUserAndSupportAndIsArchivedIsFalse(user, support)
                .orElseThrow(ConversationNotFoundException::new);
        List<PersistedChatMessage> messages = conversation.getMessages();

        return messages.stream()
                .map(m -> new ChatMessageResponse(m.getContent(), m.getSentAt(), m.getIsFromSupport()))
                .sorted(Comparator.comparing(ChatMessageResponse::getSentAt).reversed())
                .toList();
    }

    public List<ChatConversationResponse> getConversations(Authentication authentication) {
        Admin support = (Admin) authentication.getPrincipal();

        List<ChatConversation> conversations = conversationRepository.findBySupportAndIsArchivedIsFalse(support);

        return conversations.stream()
                .map(this::convertConversationToResponse)
                .sorted(Comparator.comparing(ChatConversationResponse::getDate).reversed())
                .toList();
    }

    public void sendMessageAsUser(ChatMessageRequest messageRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository
                .findByUserAndIsArchivedIsFalse(user)
                .orElseGet(() -> createNewConversation(user));

        PersistedChatMessage message = new PersistedChatMessage(messageRequest.getContent(), false);
        conversation.addMessage(message);

        messageRepository.save(message);
        conversationRepository.save(conversation);
        // TODO: Deliver over WS
    }

    public void sendMessageAsSupport(ChatMessageRequest messageRequest, UUID userId, Authentication authentication) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Admin support = (Admin) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository
                .findByUserAndSupportAndIsArchivedIsFalse(user, support)
                .orElseThrow(ConversationNotFoundException::new);

        PersistedChatMessage message = new PersistedChatMessage(messageRequest.getContent(), true);
        conversation.addMessage(message);

        messageRepository.save(message);
        conversationRepository.save(conversation);
        // TODO: Deliver over WS
    }

    private ChatConversation createNewConversation(User user) {
        Admin assignedSupport = findLeastRecentlyActiveSupport();
        return new ChatConversation(user, assignedSupport);
    }

    private Admin findLeastRecentlyActiveSupport() {
        return adminRepository.findFirstByOrderByLastActiveAt();
    }

    private ChatConversationResponse convertConversationToResponse(ChatConversation c) {
        PersistedChatMessage latestMessage = c.getMessages().get(c.getMessages().size() - 1);
        String messagePreview = previewMessage(latestMessage.getContent());
        Date date = latestMessage.getSentAt();

        return new ChatConversationResponse(
                messagePreview,
                date,
                c.getUser().getId(),
                c.getUser().getName(),
                c.getUser().getImageUrl(),
                latestMessage.getIsFromSupport());
    }

    private String previewMessage(String content) {
        return content.substring(0, Math.min(25, content.length() - 1));
    }
}
