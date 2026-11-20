package com.dss.p1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.dss.p1.model.Producto;

// esto incluye @Component scan que va a detectar automáticamente
//controladores y demás
//Clase principal, paquete raíz
@SpringBootApplication
public class P1Application {

	public static void main(String[] args) {
		SpringApplication.run(P1Application.class, args);
		Producto producto = new Producto();
		producto.getId();
		
	}

}
