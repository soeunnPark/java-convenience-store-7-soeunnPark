package store.interfaces.product;

import store.domain.inventory.ProductInventory;

public record ProductInventoryResponse(
        boolean hasPromotion,
        String productName,
        int productPrice,
        int stockQuantity,
        int promotionStockQuantity,
        String promotionName
) {

    public static ProductInventoryResponse of(ProductInventory productInventory) {
        return new ProductInventoryResponse(
                productInventory.hasPromotion(),
                productInventory.getProduct().getName(),
                productInventory.getProduct().getPrice(),
                productInventory.getStockQuantity(),
                productInventory.getPromotionStockQuantity(),
                productInventory.hasPromotion() ? productInventory.getPromotion().getName() : null
        );
    }
}
