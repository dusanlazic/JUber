package com.nwt.juber.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.nwt.juber.dto.message.MsgFromSupportMessage;
import com.nwt.juber.dto.message.MsgFromUserMessage;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public List<ChatMessageResponse> getMessages(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository.findByUserAndIsArchivedIsFalse(user).orElseThrow(ConversationNotFoundException::new);
        List<PersistedChatMessage> messages = conversation.getMessages();

        return messages.stream()
                .map(m -> new ChatMessageResponse(m.getContent(), m.getSentAt(), m.getIsFromSupport()))
                .sorted(Comparator.comparing(ChatMessageResponse::getSentAt))
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
                .sorted(Comparator.comparing(ChatMessageResponse::getSentAt))
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

        Optional<ChatConversation> optionalConversation = conversationRepository.findByUserAndIsArchivedIsFalse(user);
        ChatConversation conversation;
        boolean isNew = false;
        if(optionalConversation.isEmpty()) {
        	conversation = createNewConversation(user);
        	isNew = true;
        }
        else {
        	conversation = optionalConversation.get();
        }

        PersistedChatMessage message = new PersistedChatMessage(messageRequest.getContent(), false);

        message = messageRepository.save(message);
        conversation.addMessage(message);
        conversation = conversationRepository.save(conversation);

        if(isNew) {
        	notifyAboutNewConversation(conversation);
        }
        deliverMessage(message);
        updateSupportLastActiveAt(conversation.getSupport());
    }

    public void sendMessageAsSupport(ChatMessageRequest messageRequest, UUID userId, Authentication authentication) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Admin support = (Admin) authentication.getPrincipal();

        ChatConversation conversation = conversationRepository
                .findByUserAndSupportAndIsArchivedIsFalse(user, support)
                .orElseThrow(ConversationNotFoundException::new);

        PersistedChatMessage message = new PersistedChatMessage(messageRequest.getContent(), true);
        
        
        message = messageRepository.save(message);
        conversation.addMessage(message);
        conversation = conversationRepository.save(conversation);

        deliverMessage(message);
    }

    private ChatConversation createNewConversation(User user) {
        Admin assignedSupport = adminRepository.findFirstByOrderByLastActiveAt();
        ChatConversation conversation = new ChatConversation(user, assignedSupport);
        conversation = conversationRepository.save(conversation);

        return conversation;
    }

    private ChatConversationResponse convertConversationToResponse(ChatConversation c) {
    	ChatConversationResponse response = new ChatConversationResponse();
    	
    	response.setUserId(c.getUser().getId());
    	response.setUserFullName(c.getUser().getName());
    	response.setUserImageUrl(c.getUser().getImageUrl());
    	response.setDate(c.getLastMessageSentAt() == null ? new Date() : c.getLastMessageSentAt());
        response.setConversationId(c.getId());
    	if(c.getMessages().size() > 0) {
    		PersistedChatMessage latestMessage = c.getMessages().get(c.getMessages().size() - 1);
            String messagePreview = previewMessage(latestMessage.getContent());
            
            response.setMessagePreview(messagePreview);
            response.setIsRead(c.getIsRead());
    	}
    	

        return response;
    }

    private String previewMessage(String content) {
        return content.substring(0, Math.min(25, content.length() - 1));
    }

    private void deliverMessage(PersistedChatMessage persistedMessage) {
        if (persistedMessage.getIsFromSupport()) {
            User receiver = persistedMessage.getConversation().getUser();
            MsgFromSupportMessage transferredMessage = new MsgFromSupportMessage(persistedMessage.getContent(), persistedMessage.getSentAt());

            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/support/chat", transferredMessage);
        } else {
            User sender = persistedMessage.getConversation().getUser();
            Admin receiver = persistedMessage.getConversation().getSupport();
            MsgFromUserMessage transferredMessage = new MsgFromUserMessage(persistedMessage.getContent(), persistedMessage.getSentAt(), sender.getId());

            messagingTemplate.convertAndSendToUser(receiver.getUsername(), "/queue/support/admin/chat", transferredMessage);
        }
    }

    private void notifyAboutNewConversation(ChatConversation conversation) {
    	ChatConversationResponse newConversationMessage = convertConversationToResponse(conversation);
        messagingTemplate.convertAndSendToUser(conversation.getSupport().getUsername(), "/queue/support/admin/users", newConversationMessage);
    }

    private void updateSupportLastActiveAt(Admin support) {
        support.setLastActiveAt(new Date());
        adminRepository.save(support);
    }

    @Scheduled(cron = "0 */5 * * * *")
    private void archiveInactiveConversations() {
        Instant limit = Instant.now().minus(1, ChronoUnit.DAYS);
        List<ChatConversation> inactiveConversations = conversationRepository.findByLastMessageSentAtBeforeAndIsArchivedIsFalse(Date.from(limit));

        inactiveConversations.forEach(c -> c.setIsArchived(true));
        conversationRepository.saveAll(inactiveConversations);
    }

	public void markConversationAsRead(UUID conversationId) {
		ChatConversation conversation = conversationRepository.findById(conversationId).get();
		conversation.setIsRead(true);
		conversationRepository.save(conversation);
	}
}
