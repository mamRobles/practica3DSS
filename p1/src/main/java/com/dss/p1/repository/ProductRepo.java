package com.dss.p1.repository;
import com.dss.p1.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepo extends JpaRepository<Producto, Long> {
    // documentacion sobre los query automaticos en
    //https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    List<Producto> findByNameContainingIgnoreCase(String name);

}

