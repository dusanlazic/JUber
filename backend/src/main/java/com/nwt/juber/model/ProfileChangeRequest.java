package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ProfileChangeRequest {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    private User user;

    private boolean approved = false;

    @ElementCollection
    private Map<String, String> changes;

    private LocalDateTime requestTime;
}
