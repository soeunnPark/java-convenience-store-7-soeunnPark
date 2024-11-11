package store.domain.order;

import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryRepository;
import store.domain.product.Product;

public class OrderService {

    private final ProductInventoryRepository productInventoryRepository;

    public OrderService(ProductInventoryRepository productInventoryRepository) {
        this.productInventoryRepository = productInventoryRepository;
    }

    public int recommendAdditionalPurchase(Product product, Integer purchaseQuantity) {
        ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
        return productInventory.recommendAdditionalPurchase(purchaseQuantity);
    }

    public int getPromotionNonApplicablePurchaseQuantity(Product product, Integer purchaseQuantity) {
        ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
        return productInventory.getPromotionNonApplicablePurchaseQuantity(purchaseQuantity);
    }
}
