package com.example.demo.controllers;


import com.example.demo.dto.ProductDto;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ShopController {

    private final ProductRepository productRepository;


    public ShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/shop")
    public String getShopPage(Model model) {

        List<Product> entities = productRepository.findAll();


        List<ProductDto> products = entities.stream()
                .map(product -> new ProductDto(
                        product.getId().intValue(),
                        product.getTitle(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());

        model.addAttribute("products", products);
        return "shop";
    }
}
