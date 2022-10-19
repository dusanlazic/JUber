package com.nwt.juber.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseOk {
    private final Integer status = HttpStatus.OK.value();
    private final String message;

    public ResponseOk(String message) {
        this.message = message;
    }
}
