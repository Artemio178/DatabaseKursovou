package com.example.demo.controller;

import com.example.demo.model.PropertyRequest;
import com.example.demo.model.Realtor;
import com.example.demo.model.User;
import com.example.demo.service.PropertyRequestService;
import com.example.demo.service.RealtorService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/realtor")
public class RealtorProfileController {
    private static final Logger logger = LoggerFactory.getLogger(RealtorProfileController.class);
    
    private final RealtorService realtorService;
    private final PropertyRequestService requestService;
    private final UserService userService;

    @Autowired
    public RealtorProfileController(RealtorService realtorService, 
                                  PropertyRequestService requestService,
                                  UserService userService) {
        this.realtorService = realtorService;
        this.requestService = requestService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        logger.info("Attempting to show profile for user: {}", user.getUsername());
        try {
            Realtor realtor = realtorService.findByUser(user);
            logger.info("Found realtor: {}", realtor.getFullName());
            model.addAttribute("realtor", realtor);
            model.addAttribute("requests", requestService.findByRealtor(realtor));
            return "realtor/profile";
        } catch (Exception e) {
            logger.error("Error showing realtor profile: ", e);
            return "redirect:/properties?error=realtor_not_found";
        }
    }

    @GetMapping("/requests")
    public String showRequests(Authentication authentication,
                             @RequestParam(required = false) PropertyRequest.RequestStatus status,
                             Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Realtor realtor = realtorService.findByUser(user);
        if (status != null) {
            model.addAttribute("requests", requestService.findByRealtorAndStatus(realtor, status));
        } else {
            model.addAttribute("requests", requestService.findByRealtor(realtor));
        }
        model.addAttribute("statuses", PropertyRequest.RequestStatus.values());
        return "realtor/requests";
    }

    @PostMapping("/requests/{id}/accept")
    public String acceptRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            requestService.updateRequestStatus(id, PropertyRequest.RequestStatus.ACCEPTED);
            redirectAttributes.addFlashAttribute("successMessage", "Запрос принят");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при принятии запроса");
        }
        return "redirect:/realtor/requests";
    }

    @PostMapping("/requests/{id}/reject")
    public String rejectRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            requestService.updateRequestStatus(id, PropertyRequest.RequestStatus.REJECTED);
            redirectAttributes.addFlashAttribute("successMessage", "Запрос отклонен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при отклонении запроса");
        }
        return "redirect:/realtor/requests";
    }
} 