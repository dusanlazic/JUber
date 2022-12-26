package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class DepositAddress {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(unique = true)
    private String ethAddress;

    @ManyToOne
    private Passenger passenger;

    @Enumerated(EnumType.STRING)
    private DepositAddressStatus status = DepositAddressStatus.UNASSIGNED;

    @CreationTimestamp
    private Date created;

    @UpdateTimestamp
    private Date modified;

    public void updateModified() {
        this.modified = null;
    }

    public DepositAddress(String ethAddress) {
        this.id = UUID.randomUUID();
        this.ethAddress = ethAddress;
        this.status = DepositAddressStatus.UNASSIGNED;
    }
}
