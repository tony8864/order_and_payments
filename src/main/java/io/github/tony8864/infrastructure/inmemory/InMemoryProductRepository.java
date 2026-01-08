package io.github.tony8864.infrastructure.inmemory;

import io.github.tony8864.application.order.repository.ProductRepository;
import io.github.tony8864.domain.Product;
import io.github.tony8864.domain.vo.ProductId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryProductRepository implements ProductRepository {

    private final Map<ProductId, Product> products;

    public InMemoryProductRepository() {
        products = new HashMap<>();
    }

    @Override
    public void save(Product product) {
        products.put(product.id(), product);
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        return Optional.of(products.get(productId));
    }
}
