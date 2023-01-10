package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ProfileChangeRequest {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private Person person;

    @Enumerated(EnumType.STRING)
    private ProfileChangeRequestStatus status = ProfileChangeRequestStatus.PENDING;

    @ElementCollection
    private Map<String, String> changes;

    @CreationTimestamp
    private Date requestedAt;

    public ProfileChangeRequest(Person person, Map<String, String> changes) {
        this.person = person;
        this.changes = changes;
    }
}
