package com.nwt.juber.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PaymentInfo {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String accountNumber;
}