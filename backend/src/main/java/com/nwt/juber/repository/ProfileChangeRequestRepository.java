package com.nwt.juber.repository;

import com.nwt.juber.model.ProfileChangeRequestStatus;
import com.nwt.juber.model.ProfileChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProfileChangeRequestRepository extends JpaRepository<ProfileChangeRequest, UUID> {

    List<ProfileChangeRequest> findByStatus(ProfileChangeRequestStatus status);

    default List<ProfileChangeRequest> findPending() {
        return findByStatus(ProfileChangeRequestStatus.PENDING);
    }

}
