package com.nwt.juber.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ROLE_PASSENGER_NEW,
    ROLE_PASSENGER,
    ROLE_DRIVER,
    ROLE_ADMIN;

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(this.name());
    }
}
