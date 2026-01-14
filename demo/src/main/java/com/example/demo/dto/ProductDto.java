package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductDto(int id, String title, String description, BigDecimal price, String imageUrl) {}
