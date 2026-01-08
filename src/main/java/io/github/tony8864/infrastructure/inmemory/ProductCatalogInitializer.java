package io.github.tony8864.infrastructure.inmemory;

import io.github.tony8864.application.order.repository.ProductRepository;
import io.github.tony8864.domain.Product;
import io.github.tony8864.domain.vo.ProductId;

import java.math.BigDecimal;

public class ProductCatalogInitializer {

    private final ProductRepository productRepository;

    public ProductCatalogInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void initialize() {
        save(
                ProductId.of("00000000-0000-0000-0000-000000000001"),
                "Laptop",
                BigDecimal.valueOf(1200)
        );

        save(
                ProductId.of("00000000-0000-0000-0000-000000000002"),
                "Phone",
                BigDecimal.valueOf(800)
        );

        save(
                ProductId.of("00000000-0000-0000-0000-000000000003"),
                "Headphones",
                BigDecimal.valueOf(150)
        );
    }

    private void save(
            ProductId id,
            String name,
            BigDecimal price
    ) {
        productRepository.save(
                Product.create(id, name, price, true)
        );
    }
}
