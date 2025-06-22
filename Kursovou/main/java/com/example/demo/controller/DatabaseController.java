package com.example.demo.controller;

import com.example.demo.model.Property;
import com.example.demo.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/properties")
public class DatabaseController {

    @Autowired
    private PropertyRepository propertyRepository;

    // Получить все объекты недвижимости
    @GetMapping
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    // Получить объект недвижимости по ID
    @GetMapping("/{id}")
    public Optional<Property> getPropertyById(@PathVariable Long id) {
        return propertyRepository.findById(id);
    }

    // Создать новый объект недвижимости
    @PostMapping
    public Property createProperty(@RequestBody Property property) {
        return propertyRepository.save(property);
    }

    // Обновить существующий объект недвижимости
    @PutMapping("/{id}")
    public Property updateProperty(@PathVariable Long id, @RequestBody Property propertyDetails) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id: " + id));
        
        property.setTitle(propertyDetails.getTitle());
        property.setDescription(propertyDetails.getDescription());
        property.setAddress(propertyDetails.getAddress());
        property.setPrice(propertyDetails.getPrice());
        property.setRooms(propertyDetails.getRooms());
        property.setArea(propertyDetails.getArea());
        property.setType(propertyDetails.getType());
        property.setStatus(propertyDetails.getStatus());
        
        return propertyRepository.save(property);
    }

    // Удалить объект недвижимости
    @DeleteMapping("/{id}")
    public void deleteProperty(@PathVariable Long id) {
        propertyRepository.deleteById(id);
    }

    // Поиск по цене (меньше указанной)
    @GetMapping("/search/price-less-than/{price}")
    public List<Property> findByPriceLessThan(@PathVariable double price) {
        return propertyRepository.findByPriceLessThan(price);
    }

    // Поиск по типу недвижимости
    @GetMapping("/search/type/{type}")
    public List<Property> findByType(@PathVariable String type) {
        return propertyRepository.findByType(type);
    }
} 