package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String login() {
        logger.info("Showing login page");
        return "auth/login";
    }

    @GetMapping("/auth/register")
    public String showRegistrationForm() {
        logger.info("Showing registration form");
        return "auth/register";
    }

    @PostMapping("/auth/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String email,
            @RequestParam String fullName,
            @RequestParam String phone,
            Model model) {
        
        logger.info("Processing registration for user: {}", username);
        
        try {
            // Проверка совпадения паролей
            if (!password.equals(confirmPassword)) {
                logger.warn("Password mismatch for user: {}", username);
                model.addAttribute("error", "Пароли не совпадают");
                return "auth/register";
            }

            // Проверка существования пользователя
            if (userService.findByUsername(username) != null) {
                logger.warn("Username already exists: {}", username);
                model.addAttribute("error", "Пользователь с таким именем уже существует");
                return "auth/register";
            }

            // Создание нового пользователя
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setRole("ROLE_USER");

            userService.registerNewUser(user);
            logger.info("Successfully registered new user: {}", username);

            return "redirect:/auth/login?registered";
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/auth/home")
    public String home(Authentication authentication) {
        logger.info("Processing home request for user: {}", 
            authentication != null ? authentication.getName() : "anonymous");
        
        if (authentication != null) {
            logger.info("User authorities: {}", authentication.getAuthorities());
            
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_REALTOR"))) {
                logger.info("Redirecting to realtor profile");
                return "redirect:/realtor/profile";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                logger.info("Redirecting to user profile");
                return "redirect:/user/profile";
            }
        }
        logger.info("Redirecting to properties list");
        return "redirect:/properties";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/properties";
    }

    @GetMapping("/auth/access-denied")
    public String accessDenied() {
        logger.warn("Access denied page accessed");
        return "auth/access-denied";
    }
} 