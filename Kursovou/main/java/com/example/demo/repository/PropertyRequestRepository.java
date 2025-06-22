package com.example.demo.repository;

import com.example.demo.model.PropertyRequest;
import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyRequestRepository extends JpaRepository<PropertyRequest, Long> {
    List<PropertyRequest> findByRealtorOrderByCreatedAtDesc(Realtor realtor);
    List<PropertyRequest> findByRealtorAndStatusOrderByCreatedAtDesc(Realtor realtor, PropertyRequest.RequestStatus status);
    List<PropertyRequest> findByClient(User client);
} 