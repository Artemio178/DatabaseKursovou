package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/properties")
public class PropertyFormController {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RealtorRepository realtorRepository;

    private final Path uploadDir = Paths.get("uploads");

    @GetMapping("/new")
    public String showNewPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("realtors", realtorRepository.findByIsActiveTrue());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("propertyStatuses", PropertyStatus.values());
        return "properties/form";
    }

    @PostMapping("/save")
    public String saveProperty(@ModelAttribute Property property,
                             @RequestParam("image") MultipartFile image,
                             RedirectAttributes redirectAttributes) {
        try {
            if (!image.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Files.createDirectories(uploadDir);
                Files.copy(image.getInputStream(), uploadDir.resolve(filename));
                property.setImageFilename(filename);
            } else {
                property.setImageFilename(null);
            }

            propertyRepository.save(property);
            redirectAttributes.addFlashAttribute("success", "Объект недвижимости успешно сохранен!");
            return "redirect:/properties";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении изображения: " + e.getMessage());
            return "redirect:/properties/new";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditPropertyForm(@PathVariable Long id, Model model) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID объекта: " + id));
        model.addAttribute("property", property);
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("realtors", realtorRepository.findByIsActiveTrue());
        model.addAttribute("propertyTypes", PropertyType.values());
        model.addAttribute("propertyStatuses", PropertyStatus.values());
        return "properties/form";
    }

    @PostMapping("/update/{id}")
    public String updateProperty(@PathVariable Long id,
                               @ModelAttribute Property property,
                               @RequestParam("image") MultipartFile image,
                               RedirectAttributes redirectAttributes) {
        try {
            Property existingProperty = propertyRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный ID объекта: " + id));

            if (!image.isEmpty()) {
                String filename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Files.createDirectories(uploadDir);
                Files.copy(image.getInputStream(), uploadDir.resolve(filename));
                existingProperty.setImageFilename(filename);
            } else if (existingProperty.getImageFilename() == null) {
                existingProperty.setImageFilename(null);
            }

            existingProperty.setTitle(property.getTitle());
            existingProperty.setDescription(property.getDescription());
            existingProperty.setAddress(property.getAddress());
            existingProperty.setPrice(property.getPrice());
            existingProperty.setRooms(property.getRooms());
            existingProperty.setArea(property.getArea());
            existingProperty.setType(property.getType());
            existingProperty.setStatus(property.getStatus());
            existingProperty.setOwner(property.getOwner());

            propertyRepository.save(existingProperty);
            redirectAttributes.addFlashAttribute("success", "Объект недвижимости успешно обновлен!");
            return "redirect:/properties";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении изображения: " + e.getMessage());
            return "redirect:/properties/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProperty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propertyRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Объект недвижимости успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении объекта: " + e.getMessage());
        }
        return "redirect:/properties";
    }
} 