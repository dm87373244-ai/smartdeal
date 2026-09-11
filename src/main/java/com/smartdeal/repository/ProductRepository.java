package com.smartdeal.repository;

import com.smartdeal.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByRatingGreaterThanEqual(double rating);

    Product findFirstByNameAndRatingGreaterThanEqualOrderByPriceAsc(
            String name, double rating);

    List<Product> findByNameContainingIgnoreCaseAndRatingGreaterThanEqualOrderByPriceAsc(
            String name, double rating);

    // 👇 YE NAYA METHOD YAHAN ADD KARO
    Product findFirstByNameContainingIgnoreCaseAndRatingGreaterThanEqualOrderByPriceAsc(
            String name, double rating);
}