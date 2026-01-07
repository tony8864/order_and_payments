package io.github.tony8864.domain;

import io.github.tony8864.domain.vo.CustomerId;
import io.github.tony8864.domain.vo.EmailAddress;

import java.util.Objects;

public class Customer {
    private final CustomerId id;
    private final EmailAddress email;
    private final String name;

    private Customer(
            CustomerId id,
            EmailAddress email,
            String name
    ) {
        this.id = Objects.requireNonNull(id);
        this.email = Objects.requireNonNull(email);
        this.name = Objects.requireNonNull(name);
    }

    public Customer newCustomer(CustomerId id, EmailAddress email, String name) {
        return new Customer(id, email, name);
    }
}
