package com.example.demo.service;

import com.example.demo.model.PropertyRequest;
import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import com.example.demo.repository.PropertyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class PropertyRequestService {
    private static final Logger logger = LoggerFactory.getLogger(PropertyRequestService.class);
    
    @Autowired
    private PropertyRequestRepository propertyRequestRepository;

    @Transactional
    public PropertyRequest createRequest(PropertyRequest request) {
        return propertyRequestRepository.save(request);
    }

    public List<PropertyRequest> findByRealtor(Realtor realtor) {
        return propertyRequestRepository.findByRealtorOrderByCreatedAtDesc(realtor);
    }

    public List<PropertyRequest> findByRealtorAndStatus(Realtor realtor, PropertyRequest.RequestStatus status) {
        return propertyRequestRepository.findByRealtorAndStatusOrderByCreatedAtDesc(realtor, status);
    }

    @Transactional
    public PropertyRequest updateRequestStatus(Long requestId, PropertyRequest.RequestStatus status) {
        PropertyRequest request = propertyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        return propertyRequestRepository.save(request);
    }

    public List<PropertyRequest> findByClient(User client) {
        logger.info("Finding requests for client: {}", client.getUsername());
        List<PropertyRequest> requests = propertyRequestRepository.findByClient(client);
        logger.info("Found {} requests for client {}", requests.size(), client.getUsername());
        return requests;
    }

    public PropertyRequest save(PropertyRequest request) {
        return propertyRequestRepository.save(request);
    }

    public void delete(Long id) {
        propertyRequestRepository.deleteById(id);
    }
} 