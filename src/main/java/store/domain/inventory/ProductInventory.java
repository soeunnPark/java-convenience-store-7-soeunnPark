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
        if (totalStockQuantitiy < purchaseQuantity) {
            throw new InvalidPurchaseQuantityException(this.product, this.totalStockQuantitiy, purchaseQuantity);
        }
    }

    public int recommendAdditionalPurchase(int purchaseQuantity) {
        if (this.promotion == null) {
            return 0;
        }
        if (purchaseQuantity >= promotionStockQuantity) {
            return 0;
        }
        if ((purchaseQuantity + promotion.getGet()) % (promotion.getBuy() + promotion.getGet()) == 0) {
            return promotion.getGet();
        }
        return 0;
    }

    public int getPromotionNonApplicablePurchaseQuantity(int purchaseQuantity) {
        if (this.promotion == null) {
            return 0;
        }
        if (purchaseQuantity < promotionStockQuantity) {
            return 0;
        }
        return purchaseQuantity - getPromotionApplicablePurchaseQuantity(purchaseQuantity);
    }

    private int getPromotionApplicablePurchaseQuantity(int purchaseQuantity) {
        return (promotionStockQuantity  / (this.promotion.getBuy() + this.promotion.getGet())) * (this.promotion.getBuy() + this.promotion.getGet());
    }
}
