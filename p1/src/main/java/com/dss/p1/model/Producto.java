package com.dss.p1.model;
 
import jakarta.persistence.*;
import lombok.*;
 
import java.util.Objects;
 
@Entity 
@Getter @Setter @NoArgsConstructor @AllArgsConstructor 
@Table(name = "producto")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double price;
    // Getterse y setters hechos con lombook no es necesario
 
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto product = (Producto) o;
        return Objects.equals(id, product.id);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }   
}
 