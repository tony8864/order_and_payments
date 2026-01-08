package io.github.tony8864.application.order.repository;

import io.github.tony8864.domain.Product;
import io.github.tony8864.domain.vo.ProductId;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId productId);
}
