package store.domain.inventory;

import java.util.Objects;
import store.common.exception.InvalidPurchaseQuantityException;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

public class ProductInventory {
    private final Product product;
    private final Promotion promotion;
    private int stockQuantity;
    private int promotionStockQuantity;
    private int totalStockQuantity;

    public ProductInventory(Product product, Promotion promotion) {
        this.product = product;
        this.promotion = promotion;
    }

    public ProductInventory(Product product, Promotion promotion, int stockQuantity, int promotionStockQuantity) {
        this.product = product;
        this.promotion = promotion;
        this.stockQuantity = stockQuantity;
        this.promotionStockQuantity = promotionStockQuantity;
        this.totalStockQuantity = stockQuantity + promotionStockQuantity;
    }

    public void updateStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.totalStockQuantity = stockQuantity + this.promotionStockQuantity;
    }

    public void updatePromotionStockQuantity(int promotionStockQuantity) {
        this.promotionStockQuantity = promotionStockQuantity;
        this.totalStockQuantity = this.stockQuantity + promotionStockQuantity;
    }

    public void validateStockAvailable(int purchaseQuantity) {
        if (totalStockQuantity < purchaseQuantity) {
            throw new InvalidPurchaseQuantityException(this.product, this.totalStockQuantity, purchaseQuantity);
        }
    }

    public void purchase(int purchaseQuantity) {
        if(this.promotion == null || !this.promotion.isPromotionAvailable()) {
            stockQuantity -= purchaseQuantity;
            totalStockQuantity -= purchaseQuantity;
            return;
        }
        int promotionApplicablePurchaseQuantity = Math.min(purchaseQuantity, this.promotionStockQuantity);

        this.promotionStockQuantity -= promotionApplicablePurchaseQuantity;
        int remainingPurchaseQuantity = purchaseQuantity - promotionApplicablePurchaseQuantity;
        this.stockQuantity -= remainingPurchaseQuantity;
        this.totalStockQuantity -= purchaseQuantity;
    }

    public boolean hasPromotion() {
        return Objects.nonNull(promotion);
    }

    public int recommendAdditionalPurchase(int purchaseQuantity) {
        if (this.promotion == null || !promotion.isPromotionAvailable()) {
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
        if (this.promotion == null || !promotion.isPromotionAvailable()) {
            return 0;
        }
        if (purchaseQuantity < this.promotionStockQuantity) {
            return 0;
        }
        return purchaseQuantity - getPromotionApplicablePurchaseQuantity(purchaseQuantity);
    }

    public int getPromotionGiveawayCount(int purchaseQuantity) {
        if (this.promotion == null || !promotion.isPromotionAvailable()) {
            return 0;
        }
        return (Math.min(promotionStockQuantity, purchaseQuantity) / (this.promotion.getBuy() + this.promotion.getGet())) * this.promotion.getGet();
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public Product getProduct() {
        return product;
    }

    public int getTotalStockQuantity() {
        return this.totalStockQuantity;
    }

    public int getPromotionStockQuantity() {
        return this.promotionStockQuantity;
    }

    public int getStockQuantity() {
        return this.stockQuantity;
    }

    private int getPromotionApplicablePurchaseQuantity(int purchaseQuantity) {
        if (this.promotion == null || !promotion.isPromotionAvailable()) {
            return 0;
        }
        return (Math.min(this.promotionStockQuantity, purchaseQuantity)  / (this.promotion.getBuy() + this.promotion.getGet())) * (this.promotion.getBuy() + this.promotion.getGet());
    }
}
