package store.domain.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import store.common.exception.ProductNotFoundException;
import store.domain.product.Product;

public class StoreInventory {
    private final List<ProductInventory> storeInventory;

    public StoreInventory() {
        this.storeInventory = new ArrayList<>();
    }

    public void addProductInventory(ProductInventory productInventory) {
        this.storeInventory.add(productInventory);
    }

    public boolean existsByProduct(Product product) {
        return storeInventory.stream()
                .anyMatch(productInventory -> productInventory.getProduct().equals(product));
    }

    public ProductInventory getProductInventory(Product product) {
        for (ProductInventory productInventory : storeInventory) {
            if(productInventory.getProduct() == product) {
                return productInventory;
            }
        }
        throw new ProductNotFoundException(product);
    }

    public List<ProductInventory> getStoreInventory() {
        return Collections.unmodifiableList(storeInventory);
    }
}

