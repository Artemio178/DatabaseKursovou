package com.example.demo.controller;

import com.example.demo.model.PropertyRequest;
import com.example.demo.model.User;
import com.example.demo.service.PropertyRequestService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRequestService propertyRequestService;

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        List<PropertyRequest> requests = propertyRequestService.findByClient(user);
        model.addAttribute("requests", requests);
        return "user/profile";
    }
} 