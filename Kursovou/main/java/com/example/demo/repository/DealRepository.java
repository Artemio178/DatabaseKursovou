package com.example.demo.repository;

import com.example.demo.model.Deal;
import com.example.demo.model.DealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
    List<Deal> findByBuyerId(Long buyerId);
    List<Deal> findBySellerId(Long sellerId);
    List<Deal> findByRealtorId(Long realtorId);
    List<Deal> findByStatus(DealStatus status);
    List<Deal> findByPropertyId(Long propertyId);
} 