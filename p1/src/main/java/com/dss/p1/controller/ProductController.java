package com.dss.p1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.dss.p1.model.Producto;
import com.dss.p1.service.ProductoService;


@Controller
@RequestMapping("/products")
public class ProductController {
	@Autowired
    private ProductoService productService;
	@GetMapping
    public String getProductsPage(@RequestParam(required = false) String name,
    Model model) {
        boolean busqueda = (name != null);
        if(busqueda){
            model.addAttribute("products", productService.buscaProductos(name));
        }
        else model.addAttribute("products", productService.getAllProducts());
        //y vamos a dejar los filtros en el formulario
        model.addAttribute("name", name);

        return "products"; // Devuelve products.html desde las plantillas
    }
	@GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Producto());
        return "product_form"; // templates/product_form.html
    }
	@PostMapping("/add")
	   public String addProduct(@RequestParam String name, @RequestParam double price) {
	        Producto product = new Producto();
	        product.setName(name);
	        product.setPrice(price);
	        productService.saveProduct(product);
	        return "redirect:/admin";  // Redirigir correctamente a /admin
	    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Producto producto = productService.getProductById(id);
        model.addAttribute("product", producto);
        return "edit_product";
    }
    
	@PostMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price) {
        Producto product = productService.getProductById(id);
        if (product != null) {
            product.setName(name);
            product.setPrice(price);
            productService.saveProduct(product);
        }
        return "redirect:/admin";  // Redirigir correctamente a /admin
    }
	@PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin";  // Redirigir correctamente a /admin
    }
}