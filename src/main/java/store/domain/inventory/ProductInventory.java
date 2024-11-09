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

    public int recommendAdditionalPurchase(int purchaseQuantity) {
        if(this.promotion == null) return 0;
        if(promotion.getBuy() > purchaseQuantity) {
            return 0;
        }
        int recommendedAdditionalPurchaseQuantity = 0;
        // 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 무료로 받을 수 있는 상품 개수 반환
        if(purchaseQuantity % promotion.getBuy() == 0) {
            recommendedAdditionalPurchaseQuantity = purchaseQuantity / promotion.getBuy() * promotion.getGet();
        }
        if(purchaseQuantity + recommendedAdditionalPurchaseQuantity < promotionStockQuantity) {
            return recommendedAdditionalPurchaseQuantity;
        }
        return promotionStockQuantity - purchaseQuantity;
    }
}
