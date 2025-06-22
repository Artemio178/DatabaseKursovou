package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.service.PropertyRequestService;
import com.example.demo.service.RealtorService;
import com.example.demo.service.PropertyService;
import com.example.demo.repository.RealtorRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/properties")
public class PropertyController {
    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    private final PropertyService propertyService;
    private final PropertyRequestService requestService;
    private final RealtorRepository realtorRepository;
    private final UserService userService;

    @Autowired
    public PropertyController(PropertyService propertyService,
                            PropertyRequestService requestService,
                            RealtorRepository realtorRepository,
                            UserService userService) {
        this.propertyService = propertyService;
        this.requestService = requestService;
        this.realtorRepository = realtorRepository;
        this.userService = userService;
    }

    @GetMapping
    public String listProperties(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) PropertyType type,
            @RequestParam(required = false) Integer rooms,
            @RequestParam(required = false) Double minArea,
            @RequestParam(required = false) Double maxArea,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, getSort(sort));
        Page<Property> properties = propertyService.findAll(pageable);

        // Применяем фильтры
        if (minPrice != null || maxPrice != null || type != null || rooms != null ||
            minArea != null || maxArea != null || address != null || keyword != null) {
            
            List<Property> filteredProperties = properties.getContent().stream()
                .filter(p -> minPrice == null || p.getPrice().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.getPrice().compareTo(maxPrice) <= 0)
                .filter(p -> type == null || p.getType() == type)
                .filter(p -> rooms == null || p.getRooms() == rooms)
                .filter(p -> minArea == null || p.getArea() >= minArea)
                .filter(p -> maxArea == null || p.getArea() <= maxArea)
                .filter(p -> address == null || p.getAddress().toLowerCase().contains(address.toLowerCase()))
                .filter(p -> keyword == null || 
                    p.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .toList();

            properties = new PageImpl<>(filteredProperties, pageable, filteredProperties.size());
        }

        model.addAttribute("properties", properties);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", properties.getTotalPages());
        model.addAttribute("totalItems", properties.getTotalElements());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("propertyStatuses", PropertyStatus.values());

        // Добавляем параметры фильтрации для сохранения состояния формы
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("selectedType", type);
        model.addAttribute("rooms", rooms);
        model.addAttribute("minArea", minArea);
        model.addAttribute("maxArea", maxArea);
        model.addAttribute("address", address);
        model.addAttribute("sort", sort);
        model.addAttribute("keyword", keyword);

        return "properties/list";
    }

    private Sort getSort(String sort) {
        if (sort == null) {
            return Sort.by("id").descending();
        }

        return switch (sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "area_asc" -> Sort.by("area").ascending();
            case "area_desc" -> Sort.by("area").descending();
            case "rooms_asc" -> Sort.by("rooms").ascending();
            case "rooms_desc" -> Sort.by("rooms").descending();
            default -> Sort.by("id").descending();
        };
    }

    @GetMapping("/{id}")
    public String showPropertyDetails(@PathVariable Long id, Model model) {
        Optional<Property> property = propertyService.findById(id);
        if (property.isPresent()) {
            model.addAttribute("property", property.get());
            return "properties/details";
        }
        return "redirect:/properties";
    }

    @PostMapping("/{id}/request")
    public String createRequest(@PathVariable Long id,
                              @RequestParam String message,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            logger.info("Creating request for property {} by user {}", id, user.getUsername());
            
            Property property = propertyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID объекта недвижимости"));
            
            // Получаем риэлтора напрямую из базы данных
            List<Realtor> activeRealtors = realtorRepository.findByIsActiveTrue();
            if (activeRealtors.isEmpty()) {
                throw new RuntimeException("Нет активных риэлторов");
            }
            Realtor realtor = activeRealtors.get(0);
            
            PropertyRequest request = new PropertyRequest();
            request.setProperty(property);
            request.setClient(user);
            request.setRealtor(realtor);
            request.setMessage(message);
            request.setStatus(PropertyRequest.RequestStatus.PENDING);
            
            PropertyRequest savedRequest = requestService.createRequest(request);
            logger.info("Request created successfully with ID: {}", savedRequest.getId());
            
            redirectAttributes.addFlashAttribute("successMessage", "Запрос успешно отправлен");
        } catch (Exception e) {
            logger.error("Error creating request: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отправке запроса: " + e.getMessage());
        }
        return "redirect:/properties/" + id;
    }
} 