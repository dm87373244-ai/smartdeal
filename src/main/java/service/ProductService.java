package com.smartdeal.service;

import com.smartdeal.dto.ApiProduct;
import com.smartdeal.dto.ApiProductResponse;
import com.smartdeal.entity.Product;
import com.smartdeal.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final RestClient restClient;

    public ProductService(ProductRepository repository) {
        this.repository = repository;

        this.restClient = RestClient.builder()
                .baseUrl("https://dummyjson.com")
                .build();
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
                .filter(product -> product.getRating() >= 3.5)
                .filter(product -> {

                    String productName =
                            normalizeText(product.getName());

                    return keywords.stream()
                            .allMatch(productName::contains);
                })
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
        return searchProducts(name);
    }

    // ================= ADD PRODUCT =================
    public Product addProduct(Product product) {

        if (product.getRating() < 3.5 ||
                product.getRating() > 5.0) {

            throw new IllegalArgumentException(
                    "Product rating must be between 3.5 and 5.0"
            );
        }

        return repository.save(product);
    }

    // ================= UPDATE PRODUCT =================
    public Product updateProduct(Long id, Product newProduct) {

        if (newProduct.getRating() < 3.5 ||
                newProduct.getRating() > 5.0) {

            throw new IllegalArgumentException(
                    "Product rating must be between 3.5 and 5.0"
            );
        }

        Product existingProduct =
                repository.findById(id).orElse(null);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(newProduct.getName());
        existingProduct.setCategory(newProduct.getCategory());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setRating(newProduct.getRating());
        existingProduct.setMarketplace(newProduct.getMarketplace());
        existingProduct.setImage(newProduct.getImage());
        existingProduct.setProductUrl(newProduct.getProductUrl());

        return repository.save(existingProduct);
    }

    // ================= DELETE PRODUCT =================
    public String deleteProduct(Long id) {

        if (!repository.existsById(id)) {
            return "Product not found";
        }

        repository.deleteById(id);

        return "Product deleted successfully";
    }

    // ================= TEXT NORMALIZATION =================
    private String normalizeText(String text) {

        if (text == null) {
            return "";
        }

        text = text.trim();
        text = text.toLowerCase();

        text = text.replaceAll(
                "([a-zA-Z])([0-9])",
                "$1 $2"
        );

        text = text.replaceAll(
                "([0-9])([a-zA-Z])",
                "$1 $2"
        );

        text = text.replaceAll(
                "\\s+",
                " "
        );

        return text;
    }

    // ================= DUMMY JSON API =================
    public List<ApiProduct> searchFromApi(String name) {

        ApiProductResponse response = restClient.get()
                .uri("/products/search?q={name}", name)
                .retrieve()
                .body(ApiProductResponse.class);

        if (response == null ||
                response.getProducts() == null) {

            return List.of();
        }

        return response.getProducts()
                .stream()
                .filter(product ->
                        product.getRating() != null &&
                                product.getRating() >= 3.5
                )
                .sorted(
                        Comparator.comparingDouble(
                                ApiProduct::getPrice
                        )
                )
                .toList();
    }
}