package com.nwt.juber.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Role implements GrantedAuthority {

    @Id
    private Long id;

    private String title;

    @Override
    public String getAuthority() {
        return title;
    }

    public Role() { }

    public Role(String title) {
        this.title = title;
    }
}
