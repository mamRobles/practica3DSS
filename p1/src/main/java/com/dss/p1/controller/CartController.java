package com.dss.p1.controller;
import com.dss.p1.model.Producto;
import com.dss.p1.service.ProductoService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/cart")
@Controller
@SessionAttributes("cartItems")
public class CartController {
	//para la persistencia entre sesiones vamos a implementar una cookie
	// para no afectar a la arquitectura de la aplicación ya q me obligaría a reimplementar la mayor parte
	// de cart controller
	private static final String CART_COOKIE = "cart";
    private static final String SEPARATOR = "-";

	private final ProductoService productService;

	  public CartController(ProductoService productService) {
	    this.productService = productService;
	  }
	  //como vamos a mantener el carrito entre sesiones, solo lo creamos de cero si no está ya en la sesión
	  @ModelAttribute("cartItems")
	  public List<Producto> cartItems(@CookieValue(name = CART_COOKIE, defaultValue = "") String cookie) { 
		
		List<Producto> carritoNuevo = new ArrayList<>(); 
		if (cookie.isBlank()) return carritoNuevo; //aquí implica que no tenemos cookie de sesión
		for (String token : cookie.split(SEPARATOR)){
			try {
				Producto p = productService.getProductById(Long.parseLong(token)); // la cookie va a guardar los id de los productos de la sesión
										//así que vamos a sacarlos del service (que accede al repo) para añadirlos a nuestro carrito
				if(p !=null) carritoNuevo.add(p); //si no es nulo (por haber borrado el producto) lo añadimos al carrito
			} catch (NumberFormatException ignored){
				//se ignoran las cookies rotas (problemas de corrupción)
			}
		}
		return carritoNuevo;
	}
	  
	  @PostMapping("/clear")
	  public String vaciarCart(@ModelAttribute("cartItems") List<Producto> cartItems, HttpServletResponse response) {
	  	//vaciamos carrito
	  	cartItems.clear(); //que redirige a deleteall de repo
		guardarCarritoCookie(cartItems, response);
	  	return "redirect:/cart"; //volvemos al carrito
	  }
	  
	  @PostMapping("/checkout")
	  public String checkout(RedirectAttributes redirectAttributes,@ModelAttribute("cartItems") List<Producto> cartItems,
							HttpServletResponse response) {
	  	//Vamos a calcular un total de precios de los items y mostrarlo
		List<Producto> productos = productService.getAllProducts();
		double total = calcularTotal(productos);
		cartItems.clear();
		guardarCarritoCookie(cartItems, response); //para guardar carrito entre sesiones

		redirectAttributes.addFlashAttribute("msg", total);

	  	return "redirect:/cart";
	  }
	  private double calcularTotal(@ModelAttribute("cartItems") List<Producto> cartItems){
		double total=0;		
		//itero por todos los productos del carrito, extraigo su precio
		// y lo sumo a un total
		for (int i = 0; i<cartItems.size(); i++){
			total += cartItems.get(i).getPrice();
		}
		return total;

	  }
	  @GetMapping
	  public String cart(Model model) { return "cart"; }

	  @PostMapping("/add/{id}")
	  public String addToCart(@PathVariable("id") Long id,
	                          @ModelAttribute("cartItems") List<Producto> cartItems,
	                          HttpServletResponse response, RedirectAttributes ra) {
	    Producto p = productService.getProductById(id); // o getProductById(id)
	    if (p != null) {
	      cartItems.add(p);
			guardarCarritoCookie(cartItems, response); //para guardar carrito entre sesiones

	      ra.addFlashAttribute("msg", "Añadido al carrito");
	    }
	    return "redirect:/cart";
	  }

	  @PostMapping("/remove/{id}")
	  public String removeFromCart(@PathVariable("id") Long id,
	                               @ModelAttribute("cartItems") List<Producto> cartItems,
	                               HttpServletResponse response, RedirectAttributes ra) {
	    for (Iterator<Producto> it = cartItems.iterator(); it.hasNext();) {
	      if (it.next().getId().equals(id)) { it.remove(); break; }
	    }
		guardarCarritoCookie(cartItems, response); //para guardar carrito entre sesiones
	    ra.addFlashAttribute("msg", "Eliminado del carrito");
	    return "redirect:/cart";
	  }
	  private void guardarCarritoCookie(List<Producto> items, HttpServletResponse response){
		//esto va a guardar todos los id de los productos del carrito dentro de la cookie en un string
		String productos = items.stream().map(p->String.valueOf(p.getId())).collect(Collectors.joining(SEPARATOR));
		//vamos a crear la cookie con los productos, llaamad CART_COOKIE, en el root de nuestra web, pa http porque es el protocolo que usamos
		Cookie cookie = new Cookie(CART_COOKIE, productos);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(items.isEmpty() ? 0 : 60 * 60 * 24 * 30); // segundos
		//si la cookie estça vacía (porque no hay nada en el carrito) entonces se borra, pero sino 
		// la cookie dura:
		// x60 minutos
		// x60 horas
		// x24 día
		// x30 -> 30 días hechos segundos
		response.addCookie(cookie);
	  }
}
