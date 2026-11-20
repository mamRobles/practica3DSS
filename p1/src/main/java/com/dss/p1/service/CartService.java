package com.dss.p1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dss.p1.model.Producto;
import com.dss.p1.repository.ProductRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor 
public class CartService {
    @Autowired
    private ProductRepo productRepo;

    public List<Producto> getAllProducts() {
        return productRepo.findAll();
    }
    public void saveProduct(Producto product) {
        productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
    public Producto getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }
}
