package com.dss.p1.controller;

import com.dss.p1.service.CartService;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dss.p1.model.Producto;
import com.dss.p1.service.ProductoService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final CartService cartService;
    @Autowired
    private ProductoService productService;

    ApiController(CartService cartService) {
        this.cartService = cartService;
    }

     
    @GetMapping("/products")
    public List<Producto> getProducts() {
        return productService.getAllProducts().stream()
                .sorted(Comparator.comparing(Producto::getName))
                .toList();
    }
    @GetMapping("/cart")
    public List<Producto> getCart(){
        return cartService.getAllProducts().stream()
            .sorted(Comparator.comparing(Producto::getName))
                .toList();
    }
}