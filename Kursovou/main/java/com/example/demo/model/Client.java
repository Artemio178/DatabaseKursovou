package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @OneToMany(mappedBy = "buyer")
    private List<Deal> dealsAsBuyer;

    @OneToMany(mappedBy = "seller")
    private List<Deal> dealsAsSeller;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<Deal> getDealsAsBuyer() {
        return dealsAsBuyer;
    }

    public void setDealsAsBuyer(List<Deal> dealsAsBuyer) {
        this.dealsAsBuyer = dealsAsBuyer;
    }

    public List<Deal> getDealsAsSeller() {
        return dealsAsSeller;
    }

    public void setDealsAsSeller(List<Deal> dealsAsSeller) {
        this.dealsAsSeller = dealsAsSeller;
    }
} 