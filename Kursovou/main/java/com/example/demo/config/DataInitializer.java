package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RealtorRepository realtorRepository;

    @Override
    public void run(String... args) {
        // Проверяем, есть ли уже данные
        if (propertyRepository.count() == 0) {
            // Создаем тестовых клиентов
            Client client1 = new Client();
            client1.setFullName("Иванов Иван Иванович");
            clientRepository.save(client1);

            Client client2 = new Client();
            client2.setFullName("Петров Петр Петрович");
            clientRepository.save(client2);

            // Создаем тестовых риэлторов
            Realtor realtor1 = new Realtor();
            realtor1.setFullName("Сидоров Алексей");
            realtor1.setPhoneNumber("+7 (999) 123-45-67");
            realtor1.setPosition("Старший риэлтор");
            realtor1.setCommissionPercentage(5.0);
            realtorRepository.save(realtor1);

            Realtor realtor2 = new Realtor();
            realtor2.setFullName("Козлова Мария");
            realtor2.setPhoneNumber("+7 (999) 765-43-21");
            realtor2.setPosition("Риэлтор");
            realtor2.setCommissionPercentage(4.5);
            realtorRepository.save(realtor2);

            // Создаем тестовые объекты недвижимости
            createTestProperties(client1, client2);
        }
    }

    private void createTestProperties(Client client1, Client client2) {
        // Квартиры
        Property apartment1 = new Property();
        apartment1.setTitle("2-комнатная квартира в центре");
        apartment1.setDescription("Светлая квартира с ремонтом");
        apartment1.setAddress("ул. Ленина, 10");
        apartment1.setPrice(BigDecimal.valueOf(3500000.00));
        apartment1.setRooms(2);
        apartment1.setArea(65.0);
        apartment1.setType(PropertyType.APARTMENT);
        apartment1.setStatus(PropertyStatus.AVAILABLE);
        apartment1.setOwner(client1);
        propertyRepository.save(apartment1);

        Property apartment2 = new Property();
        apartment2.setTitle("3-комнатная квартира");
        apartment2.setDescription("Просторная квартира с балконом");
        apartment2.setAddress("пр. Мира, 25");
        apartment2.setPrice(BigDecimal.valueOf(4500000.00));
        apartment2.setRooms(3);
        apartment2.setArea(85.0);
        apartment2.setType(PropertyType.APARTMENT);
        apartment2.setStatus(PropertyStatus.AVAILABLE);
        apartment2.setOwner(client2);
        propertyRepository.save(apartment2);

        // Дома
        Property house1 = new Property();
        house1.setTitle("Загородный дом");
        house1.setDescription("Дом с участком 10 соток");
        house1.setAddress("с. Подгорное, ул. Садовая, 5");
        house1.setPrice(BigDecimal.valueOf(8500000.00));
        house1.setRooms(5);
        house1.setArea(150.0);
        house1.setType(PropertyType.HOUSE);
        house1.setStatus(PropertyStatus.AVAILABLE);
        house1.setOwner(client1);
        propertyRepository.save(house1);

        Property house2 = new Property();
        house2.setTitle("Коттедж");
        house2.setDescription("Современный коттедж с гаражем");
        house2.setAddress("п. Солнечный, ул. Центральная, 15");
        house2.setPrice(BigDecimal.valueOf(12000000.00));
        house2.setRooms(6);
        house2.setArea(200.0);
        house2.setType(PropertyType.HOUSE);
        house2.setStatus(PropertyStatus.AVAILABLE);
        house2.setOwner(client2);
        propertyRepository.save(house2);

        // Офисы
        Property office1 = new Property();
        office1.setTitle("Офис в бизнес-центре");
        office1.setDescription("Офисное помещение с ремонтом");
        office1.setAddress("ул. Деловая, 100");
        office1.setPrice(BigDecimal.valueOf(8000000.00));
        office1.setRooms(4);
        office1.setArea(120.0);
        office1.setType(PropertyType.OFFICE);
        office1.setStatus(PropertyStatus.AVAILABLE);
        office1.setOwner(client1);
        propertyRepository.save(office1);

        Property office2 = new Property();
        office2.setTitle("Офисное помещение");
        office2.setDescription("Помещение под офис");
        office2.setAddress("пр. Строителей, 50");
        office2.setPrice(BigDecimal.valueOf(6000000.00));
        office2.setRooms(3);
        office2.setArea(90.0);
        office2.setType(PropertyType.OFFICE);
        office2.setStatus(PropertyStatus.AVAILABLE);
        office2.setOwner(client2);
        propertyRepository.save(office2);

        // Другие типы
        Property other1 = new Property();
        other1.setTitle("Складское помещение");
        other1.setDescription("Склад с подъездными путями");
        other1.setAddress("ул. Промышленная, 30");
        other1.setPrice(BigDecimal.valueOf(15000000.00));
        other1.setRooms(1);
        other1.setArea(300.0);
        other1.setType(PropertyType.OTHER);
        other1.setStatus(PropertyStatus.AVAILABLE);
        other1.setOwner(client1);
        propertyRepository.save(other1);

        Property other2 = new Property();
        other2.setTitle("Торговое помещение");
        other2.setDescription("Помещение под магазин");
        other2.setAddress("ул. Торговая, 20");
        other2.setPrice(BigDecimal.valueOf(10000000.00));
        other2.setRooms(2);
        other2.setArea(150.0);
        other2.setType(PropertyType.OTHER);
        other2.setStatus(PropertyStatus.AVAILABLE);
        other2.setOwner(client2);
        propertyRepository.save(other2);
    }
} 