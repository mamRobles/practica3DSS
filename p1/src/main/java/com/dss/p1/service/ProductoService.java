package com.dss.p1.service;

import com.dss.p1.model.Producto;
import com.dss.p1.repository.ProductRepo;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor 
public class ProductoService {
@Autowired
    private ProductRepo productRepo;
    public List<Producto> buscaProductos(String name){
        return productRepo.findByNameContainingIgnoreCase(name.trim());
    }
    public void deleteAllProducts(){
      productRepo.deleteAll();
    }
    public List<Producto> getAllProducts() {
        return productRepo.findAll();
    }

    public Producto getProductById(Long id) {
        return productRepo.findById(id).orElse(null);
    }

    public void saveProduct(Producto product) {
        productRepo.save(product);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }

}
