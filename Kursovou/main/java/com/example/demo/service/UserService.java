package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.Realtor;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RealtorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RealtorRepository realtorRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RealtorRepository realtorRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.realtorRepository = realtorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    @Transactional
    public void init() {
        logger.info("=== Starting user initialization ===");
        try {
            createDefaultUsers();
            logger.info("=== User initialization completed successfully ===");
        } catch (Exception e) {
            logger.error("Error during user initialization: ", e);
        }
    }

    @Transactional
    public void registerNewUser(User user) {
        logger.info("Registering new user: {}", user.getUsername());
        
        // Проверка существования пользователя
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("User already exists: {}", user.getUsername());
            throw new RuntimeException("User already exists");
        }

        // Хеширование пароля
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Сохранение пользователя
        userRepository.save(user);
        logger.info("Successfully registered user: {}", user.getUsername());
    }

    private void createDefaultUsers() {
        // Создаем риэлтора
        if (!userRepository.findByUsername("admin").isPresent()) {
            logger.info("Creating default realtor");
            User realtorUser = new User();
            realtorUser.setUsername("admin");
            String encodedPassword = passwordEncoder.encode("admin");
            logger.info("Encoded password for admin: {}", encodedPassword);
            realtorUser.setPassword(encodedPassword);
            realtorUser.setRole("ROLE_REALTOR");
            realtorUser.setEmail("admin@example.com");
            realtorUser.setFullName("Администратор Системы");
            realtorUser.setPhone("+7 (999) 123-45-67");
            userRepository.save(realtorUser);
            logger.info("Created realtor user: {}", realtorUser.getUsername());

            // Создаем связанного риэлтора
            Realtor realtor = new Realtor();
            realtor.setFullName(realtorUser.getFullName());
            realtor.setPhoneNumber(realtorUser.getPhone());
            realtor.setPosition("Старший риэлтор");
            realtor.setCommissionPercentage(5.0);
            realtor.setUser(realtorUser);
            realtor.setIsActive(true);
            realtorRepository.save(realtor);
            logger.info("Created realtor entity: {}", realtor.getFullName());
        } else {
            logger.info("Realtor 'admin' already exists");
        }

        // Создаем обычного пользователя
        if (!userRepository.findByUsername("user").isPresent()) {
            logger.info("Creating default user");
            User user = new User();
            user.setUsername("user");
            String encodedPassword = passwordEncoder.encode("user");
            logger.info("Encoded password for user: {}", encodedPassword);
            user.setPassword(encodedPassword);
            user.setRole("ROLE_USER");
            user.setEmail("user@example.com");
            user.setFullName("Пользователь Один");
            user.setPhone("+7 (999) 765-43-21");
            userRepository.save(user);
            logger.info("Created user: {}", user.getUsername());
        } else {
            logger.info("User 'user' already exists");
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
} 