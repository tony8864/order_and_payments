package io.github.tony8864.application.order.repository;

import io.github.tony8864.domain.Product;
import io.github.tony8864.domain.vo.ProductId;

import java.util.Optional;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(ProductId productId);
}
