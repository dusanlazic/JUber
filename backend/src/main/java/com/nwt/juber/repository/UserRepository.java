package com.nwt.juber.repository;

import com.nwt.juber.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT a FROM User a LEFT JOIN FETCH a.notifications  WHERE a.id = ?1 ")
	public Optional<User> getUserWithNotificationsById(UUID id);

    List<User> findByBlockedIsTrue();
}
