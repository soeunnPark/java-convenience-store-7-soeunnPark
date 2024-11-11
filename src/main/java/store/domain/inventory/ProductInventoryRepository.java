package store.domain.inventory;

import java.util.ArrayList;
import java.util.List;
import store.common.exception.ProductNotFoundException;
import store.domain.product.Product;

public class ProductInventoryRepository {
    private final List<ProductInventory> productInventories;

    public ProductInventoryRepository() {
        this.productInventories = new ArrayList<>();
    }

    public void saveProductInventory(ProductInventory productInventory) {
        this.productInventories.add(productInventory);
    }

    public boolean existsByProduct(Product product) {
        return productInventories.stream()
                .anyMatch(productInventory -> productInventory.getProduct().equals(product));
    }

    public ProductInventory findProductInventory(String productName) {
        for (ProductInventory productInventory : productInventories) {
            if (productInventory.getProduct().getName().equals(productName)) {
                return productInventory;
            }
        }
        throw new ProductNotFoundException(productName);
    }

    public ProductInventory getProductInventory(Product product) {
        for (ProductInventory productInventory : productInventories) {
            if(productInventory.getProduct() == product) {
                return productInventory;
            }
        }
        throw new ProductNotFoundException(product);
    }

    public List<ProductInventory> findAllProductInventory() {
        return productInventories;
    }
}

