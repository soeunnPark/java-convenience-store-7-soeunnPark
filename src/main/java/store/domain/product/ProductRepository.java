package store.domain.product;

import java.util.ArrayList;
import java.util.List;
import store.common.exception.ProductNotFoundException;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public void save(Product product) {
        products.add(product);
    }

    public boolean existByProductName(String productName) {
        return this.products.stream().anyMatch(product -> product.getName().equals(productName));
    }

    public Product findByProductName(String productName) {
        return products.stream().filter(product -> product.getName().equals(productName)).findFirst()
                .orElseThrow(() -> new ProductNotFoundException(productName));
    }

    public List<Product> findAllProducts() {
        return products;
    }
}
