package com.nwt.juber.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

// test table name
@Entity
@Data
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class User {

    @Id
    @Column(unique = true)
    private String mail;

    private String password;

}
