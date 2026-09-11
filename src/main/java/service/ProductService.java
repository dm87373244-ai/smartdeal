package com.smartdeal.service;

import com.smartdeal.entity.Product;
import com.smartdeal.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // ================= GET ALL PRODUCTS =================
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    // ================= TOP RATED PRODUCTS =================
    public List<Product> getTopRatedProducts() {
        return repository.findByRatingGreaterThanEqual(3.5);
    }

    // ================= CHEAPEST PRODUCT =================
    public Product getCheapestProduct(String name) {

        List<Product> products = searchProducts(name);

        if (products.isEmpty()) {
            return null;
        }

        return products.get(0);
    }

    // ================= SMART SEARCH =================
    public List<Product> searchProducts(String name) {

        String searchText = normalizeText(name);

        // Common words remove karo
        Set<String> ignoredWords = Set.of(
                "apple",
                "mobile",
                "phone",
                "smartphone"
        );

        List<String> keywords = Arrays.stream(searchText.split(" "))
                .filter(word -> !word.isEmpty())
                .filter(word -> !ignoredWords.contains(word))
                .toList();

        return repository.findAll()
                .stream()

                // Rating 3.5+ only
                .filter(product -> product.getRating() >= 3.5)

                // Search keywords match
                .filter(product -> {

                    String productName =
                            normalizeText(product.getName());

                    return keywords.stream()
                            .allMatch(productName::contains);
                })

                // Cheapest first
                .sorted(Comparator.comparingDouble(Product::getPrice))

                .toList();
    }

    // ================= BEST DEAL =================
    public Product getBestDeal(String name) {

        List<Product> products = searchProducts(name);

        if (products.isEmpty()) {
            return null;
        }

        return products.get(0);
    }

    // ================= GET PRODUCT BY ID =================
    public Product getProductById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // ================= RELATED PRODUCTS =================
    public List<Product> getRelatedProducts(String name) {

        // Related products bhi smart search use karenge
        return searchProducts(name);
    }

    // ================= ADD PRODUCT =================
    public Product addProduct(Product product) {

        if (product.getRating() < 3.5 || product.getRating() > 5.0) {
            throw new IllegalArgumentException(
                    "Product rating must be between 3.5 and 5.0"
            );
        }

        return repository.save(product);
    }

    // ================= UPDATE PRODUCT =================
    public Product updateProduct(Long id, Product newProduct) {

        // Rating validation
        if (newProduct.getRating() < 3.5 || newProduct.getRating() > 5.0) {
            throw new IllegalArgumentException(
                    "Product rating must be between 3.5 and 5.0"
            );
        }

        Product existingProduct =
                repository.findById(id).orElse(null);

        // Product nahi mila
        if (existingProduct == null) {
            return null;
        }

        // Existing product ki details update karo
        existingProduct.setName(newProduct.getName());
        existingProduct.setCategory(newProduct.getCategory());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setRating(newProduct.getRating());
        existingProduct.setMarketplace(newProduct.getMarketplace());
        existingProduct.setImage(newProduct.getImage());
        existingProduct.setProductUrl(newProduct.getProductUrl());

        // Updated product database me save karo
        return repository.save(existingProduct);
    }

    // ================= DELETE PRODUCT =================
    public String deleteProduct(Long id) {

        // Product exist karta hai ya nahi
        if (!repository.existsById(id)) {
            return "Product not found";
        }

        // Product delete karo
        repository.deleteById(id);

        return "Product deleted successfully";
    }

    // ================= TEXT NORMALIZATION =================
    private String normalizeText(String text) {

        if (text == null) {
            return "";
        }

        // Extra spaces remove
        text = text.trim();

        // Lowercase
        text = text.toLowerCase();

        // iphone15 -> iphone 15
        text = text.replaceAll(
                "([a-zA-Z])([0-9])",
                "$1 $2"
        );

        // 15iphone -> 15 iphone
        text = text.replaceAll(
                "([0-9])([a-zA-Z])",
                "$1 $2"
        );

        // Multiple spaces -> single space
        text = text.replaceAll(
                "\\s+",
                " "
        );

        return text;
    }
}