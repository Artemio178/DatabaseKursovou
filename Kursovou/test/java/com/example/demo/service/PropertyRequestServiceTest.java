package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.demo.model.PropertyRequest;
import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import com.example.demo.repository.PropertyRequestRepository;

class PropertyRequestServiceTest {
    @Mock
    private PropertyRequestRepository propertyRequestRepository;
    @InjectMocks
    private PropertyRequestService propertyRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_success() {
        PropertyRequest request = new PropertyRequest();
        when(propertyRequestRepository.save(request)).thenReturn(request);
        PropertyRequest saved = propertyRequestService.createRequest(request);
        assertEquals(request, saved);
    }

    @Test
    void findByRealtor() {
        Realtor realtor = new Realtor();
        List<PropertyRequest> list = Collections.singletonList(new PropertyRequest());
        when(propertyRequestRepository.findByRealtorOrderByCreatedAtDesc(realtor)).thenReturn(list);
        List<PropertyRequest> result = propertyRequestService.findByRealtor(realtor);
        assertEquals(1, result.size());
    }

    @Test
    void findByClient() {
        User user = new User();
        List<PropertyRequest> list = Collections.singletonList(new PropertyRequest());
        when(propertyRequestRepository.findByClient(user)).thenReturn(list);
        List<PropertyRequest> result = propertyRequestService.findByClient(user);
        assertEquals(1, result.size());
    }

    @Test
    void updateRequestStatus_success() {
        PropertyRequest request = new PropertyRequest();
        when(propertyRequestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(propertyRequestRepository.save(request)).thenReturn(request);
        PropertyRequest updated = propertyRequestService.updateRequestStatus(1L, PropertyRequest.RequestStatus.ACCEPTED);
        assertEquals(PropertyRequest.RequestStatus.ACCEPTED, updated.getStatus());
    }

    @Test
    void updateRequestStatus_notFound() {
        when(propertyRequestRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> propertyRequestService.updateRequestStatus(2L, PropertyRequest.RequestStatus.ACCEPTED));
    }
} 