package com.example.currency.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.Executors;

@Controller
public class CurrencyController {

    @Value("${app.openexchangerates.url}")
    private String apiUrl;

    @Value("${app.openexchangerates.app_id}")
    private String appId;

    private WebClient client;
    private List<String> currencies;

    @PostConstruct
    public void init() {
        client = WebClient.builder().baseUrl(apiUrl).build();
        currencies = Arrays.asList("USD", "EUR", "RUB", "GBP", "JPY", "CNY");
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("currencies", currencies);
        return "index";
    }

    @GetMapping("/rate")
    public String rate(@RequestParam String from, @RequestParam String to, Model model) {
        model.addAttribute("currencies", currencies);
        try {
            Map response = client.get()
                    .uri("?app_id=" + appId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Map<String, Object> rates = (Map<String, Object>) response.get("rates");
            double fromRate = ((Number) rates.get(from)).doubleValue();
            double toRate = ((Number) rates.get(to)).doubleValue();

            double rate = toRate / fromRate;
            model.addAttribute("rate", String.format("%.4f", rate));
        } catch (Exception e) {
            model.addAttribute("rate", "Error: " + e.getMessage());
        }

        return "index";
    }

    @PostMapping("/shutdown")
    public String shutdown() {
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(1000);
                System.exit(0);
            } catch (InterruptedException ignored) {}
        });
        return "redirect:/";
    }
}