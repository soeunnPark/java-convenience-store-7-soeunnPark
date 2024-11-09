package store.domain.inventory;

import java.util.List;
import store.common.exception.ProductNotFoundException;
import store.domain.product.Product;

public class StoreInventory {
    private List<ProductInventory> storeInventory;

    public StoreInventory(List<ProductInventory> storeInventory) {
        this.storeInventory = storeInventory;
    }

    public ProductInventory getProductInventory(Product product) {
        for (ProductInventory productInventory : storeInventory) {
            if(productInventory.getProduct() == product) {
                return productInventory;
            }
        }
        throw new ProductNotFoundException(product);
    }
}
