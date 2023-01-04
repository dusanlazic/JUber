package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ChatConversation {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private User user;

    @ManyToOne
    private Admin support;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<PersistedChatMessage> messages;

    private Boolean isArchived = false;

    public ChatConversation(User user, Admin support) {
        this.user = user;
        this.support = support;
    }
}
