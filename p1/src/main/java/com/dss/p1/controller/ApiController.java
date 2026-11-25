package com.dss.p1.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dss.p1.model.Producto;
import com.dss.p1.service.ProductoService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ProductoService productService;

    // Body de POST /api/checkout: {"items":[{"productId":1,"quantity":2}]}
    record CheckoutRequest(List<Item> items) {}
    record Item(Long productId, Integer quantity) {}

    @GetMapping("/products")
    public List<Producto> getProducts() {
        return productService.getAllProducts().stream()
                .sorted(Comparator.comparing(Producto::getName))
                .toList();
    }

    // El carrito vive en el móvil; aquí llega en el checkout con HTTP Basic.
    // El total se calcula con los precios de la BD, no con los que mande la app.
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req, Authentication auth) {
        if (req == null || req.items() == null || req.items().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El carrito está vacío"));
        }
        List<Map<String, Object>> lineas = new ArrayList<>();
        double total = 0;
        for (Item item : req.items()) {
            int qty = item.quantity() == null ? 1 : item.quantity();
            Producto p = item.productId() == null ? null : productService.getProductById(item.productId());
            if (p == null || qty <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto o cantidad no válidos: " + item.productId()));
            }
            lineas.add(Map.of("id", p.getId(), "name", p.getName(), "price", p.getPrice(), "quantity", qty));
            total += p.getPrice() * qty;
        }
        return ResponseEntity.ok(Map.of(
                "username", auth.getName(),
                "items", lineas,
                "total", Math.round(total * 100) / 100.0));
    }
}