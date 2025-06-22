package com.example.demo.service;

import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import com.example.demo.repository.RealtorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RealtorService {
    private final RealtorRepository realtorRepository;

    @Autowired
    public RealtorService(RealtorRepository realtorRepository) {
        this.realtorRepository = realtorRepository;
    }

    public Realtor findByUser(User user) {
        return realtorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Realtor not found for user: " + user.getUsername()));
    }

    @Transactional
    public Realtor save(Realtor realtor) {
        return realtorRepository.save(realtor);
    }
} 