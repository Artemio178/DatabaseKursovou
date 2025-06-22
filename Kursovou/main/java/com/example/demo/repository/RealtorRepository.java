package com.example.demo.repository;

import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RealtorRepository extends JpaRepository<Realtor, Long> {
    Optional<Realtor> findByUser(User user);
    List<Realtor> findByIsActiveTrue();
} 