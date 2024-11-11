package store.domain.inventory;

import java.util.ArrayList;
import java.util.List;
import store.common.exception.ProductNotFoundException;

public class ProductInventoryRepository {

    private final List<ProductInventory> productInventories;

    public ProductInventoryRepository() {
        this.productInventories = new ArrayList<>();
    }

    public void saveProductInventory(ProductInventory productInventory) {
        this.productInventories.add(productInventory);
    }

    public ProductInventory findProductInventory(String productName) {
        for (ProductInventory productInventory : productInventories) {
            if (productInventory.getProduct().getName().equals(productName)) {
                return productInventory;
            }
        }
        throw new ProductNotFoundException(productName);
    }

    public List<ProductInventory> findAllProductInventory() {
        return productInventories;
    }
}
