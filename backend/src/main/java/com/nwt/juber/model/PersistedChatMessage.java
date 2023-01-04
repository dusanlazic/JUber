package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PersistedChatMessage {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private ChatConversation conversation;

    private String content;

    private Boolean isFromSupport;

    @CreationTimestamp
    private Date sentAt;

    public PersistedChatMessage(String content, Boolean isFromSupport) {
        this.content = content;
        this.isFromSupport = isFromSupport;
    }
}
