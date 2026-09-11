
package com.smartdeal.controller;

import com.smartdeal.entity.Product;
import com.smartdeal.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // ================= GET ALL PRODUCTS =================
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    // ================= TOP RATED PRODUCTS =================
    @GetMapping("/top-rated")
    public List<Product> getTopRatedProducts() {
        return service.getTopRatedProducts();
    }

    // ================= CHEAPEST PRODUCT =================
    @GetMapping("/cheapest/{name}")
    public Product getCheapestProduct(@PathVariable String name) {
        return service.getCheapestProduct(name);
    }

    // ================= SMART SEARCH =================
    @GetMapping("/search/{name}")
    public List<Product> searchProducts(@PathVariable String name) {
        return service.searchProducts(name);
    }

    // ================= BEST DEAL =================
    @GetMapping("/best-deal/{name}")
    public Product getBestDeal(@PathVariable String name) {
        return service.getBestDeal(name);
    }

    // ================= GET PRODUCT BY ID =================
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return service.getProductById(id);
    }

    // ================= RELATED PRODUCTS =================
    @GetMapping("/related/{name}")
    public List<Product> getRelatedProducts(@PathVariable String name) {
        return service.getRelatedProducts(name);
    }

    // ================= ADD PRODUCT =================
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return service.addProduct(product);
    }

    // ================= UPDATE PRODUCT =================
    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        return service.updateProduct(id, product);
    }

    // ================= DELETE PRODUCT =================
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        return service.deleteProduct(id);
    }
}