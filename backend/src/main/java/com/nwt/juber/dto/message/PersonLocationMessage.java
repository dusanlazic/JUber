package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PersonLocationMessage {
    private String email;
    private Double latitude;
    private Double longitude;

    public PersonLocationMessage(String email, Double latitude, Double longitude) {
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
