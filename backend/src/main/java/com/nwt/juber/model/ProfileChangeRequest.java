package com.nwt.juber.model;

import com.zaxxer.hikari.util.ClockSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class ProfileChangeRequest {
    @Id
    private Long id;

    @ManyToOne
    private User user;

    private boolean approved = false;

    @ElementCollection
    private Map<String, String> changes;

    private LocalDateTime requestTime;
}
