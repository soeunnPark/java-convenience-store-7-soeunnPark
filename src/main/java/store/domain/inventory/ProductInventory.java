package store.domain.inventory;

import store.common.exception.InvalidPurchaseQuantityException;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class ProductInventory {
    private final Product product;
    private final Promotion promotion;
    private int stockQuantitiy;
    private int promotionStockQuantity;
    private int totalStockQuantitiy;

    public ProductInventory(Product product, Promotion promotion, int stockQuantity, int promotionStockQuantity) {
        this.product = product;
        this.promotion = promotion;
        this.stockQuantitiy = stockQuantity;
        this.promotionStockQuantity = promotionStockQuantity;
        this.totalStockQuantitiy = stockQuantity + promotionStockQuantity;
    }

    public void validateStockAvailable(int purchaseQuantity) {
        if(totalStockQuantitiy < purchaseQuantity) {
            throw new InvalidPurchaseQuantityException(this.product, this.totalStockQuantitiy, purchaseQuantity);
        }
    }
}
