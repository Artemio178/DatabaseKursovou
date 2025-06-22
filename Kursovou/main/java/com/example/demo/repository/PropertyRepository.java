package com.example.demo.repository;

import com.example.demo.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    @Query("SELECT p FROM Property p WHERE p.price < :price")
    List<Property> findByPriceLessThan(@Param("price") double price);
    
    @Query("SELECT p FROM Property p WHERE p.type = :type")
    List<Property> findByType(@Param("type") String type);
} 