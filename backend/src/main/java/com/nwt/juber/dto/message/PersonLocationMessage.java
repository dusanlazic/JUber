package com.nwt.juber.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonLocationMessage {
    private String email;
    private Double latitude;
    private Double longitude;
}
