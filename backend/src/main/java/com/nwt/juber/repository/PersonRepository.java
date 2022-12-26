package com.nwt.juber.repository;

import com.nwt.juber.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Boolean existsByPhoneNumber(String phoneNumber);

}
