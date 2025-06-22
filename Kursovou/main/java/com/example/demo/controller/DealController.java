package com.example.demo.controller;

import com.example.demo.model.Deal;
import com.example.demo.model.DealStatus;
import com.example.demo.model.Property;
import com.example.demo.model.Client;
import com.example.demo.repository.DealRepository;
import com.example.demo.repository.PropertyRepository;
import com.example.demo.repository.RealtorRepository;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/deals")
public class DealController {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RealtorRepository realtorRepository;

    @GetMapping
    public String listDeals(Model model) {
        model.addAttribute("deals", dealRepository.findAll());
        return "deals/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam Long propertyId, Model model) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid property Id:" + propertyId));
        
        model.addAttribute("property", property);
        model.addAttribute("deal", new Deal());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("realtors", realtorRepository.findByIsActiveTrue());
        return "deals/create";
    }

    @PostMapping("/new")
    public String createDeal(@ModelAttribute Deal deal,
                           @RequestParam Long propertyId,
                           @RequestParam Long buyerId,
                           @RequestParam Long realtorId) {
        Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid property Id:" + propertyId));
        Client buyer = clientRepository.findById(buyerId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid buyer Id:" + buyerId));
        Client seller = property.getOwner();
        
        deal.setProperty(property);
        deal.setBuyer(buyer);
        deal.setSeller(seller);
        deal.setRealtor(realtorRepository.findById(realtorId).orElse(null));
        deal.setStatus(DealStatus.PENDING);
        deal.setDealDate(LocalDateTime.now());
        
        dealRepository.save(deal);
        return "redirect:/deals";
    }

    @GetMapping("/{id}")
    public String showDealDetails(@PathVariable Long id, Model model) {
        Deal deal = dealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid deal Id:" + id));
        model.addAttribute("deal", deal);
        return "deals/details";
    }

    @PostMapping("/{id}/update-status")
    public String updateDealStatus(@PathVariable Long id, @RequestParam DealStatus status) {
        Deal deal = dealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid deal Id:" + id));
        deal.setStatus(status);
        dealRepository.save(deal);
        return "redirect:/deals/" + id;
    }
} 