package com.nwt.juber.repository;

import com.nwt.juber.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {

    Admin findFirstByOrderByLastActiveAt();

}
