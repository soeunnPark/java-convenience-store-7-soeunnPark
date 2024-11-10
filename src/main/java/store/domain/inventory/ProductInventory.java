package store.domain.inventory;

import java.time.LocalDate;
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

    public ProductInventory(Product product, Promotion promotion, int stockQuantity, int promotionStockQuantity) {
        this.product = product;
        this.promotion = promotion;
        this.stockQuantity = stockQuantity;
        this.promotionStockQuantity = promotionStockQuantity;
        this.totalStockQuantity = stockQuantity + promotionStockQuantity;
    }

    public void validateStockAvailable(int purchaseQuantity) {
        if (totalStockQuantity < purchaseQuantity) {
            throw new InvalidPurchaseQuantityException(this.product, this.totalStockQuantity, purchaseQuantity);
        }
    }

    public void purchase(int purchaseQuantity) {
        if(this.promotion == null) {
            stockQuantity -= purchaseQuantity;
            totalStockQuantity -= purchaseQuantity;
            return;
        }
        int promotionApplicablePurchaseQuantity = getPromotionApplicablePurchaseQuantity(purchaseQuantity);
        this.promotionStockQuantity -= promotionApplicablePurchaseQuantity;
        this.stockQuantity -= (purchaseQuantity - promotionApplicablePurchaseQuantity);
        this.totalStockQuantity -= purchaseQuantity;
    }

    public boolean hasPromotion() {
        return Objects.nonNull(promotion);
    }

    public int recommendAdditionalPurchase(int purchaseQuantity) {
        if (this.promotion == null || !promotion.isApplicable(LocalDate.now())) {
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
        if (this.promotion == null || !promotion.isApplicable(LocalDate.now())) {
            return 0;
        }
        if (purchaseQuantity < this.promotionStockQuantity) {
            return 0;
        }
        return purchaseQuantity - getPromotionApplicablePurchaseQuantity(purchaseQuantity);
    }

    public int getPromotionGiveawayCount(int purchaseQuantity) {
        if (this.promotion == null || !promotion.isApplicable(LocalDate.now())) {
            return 0;
        }
        return (Math.min(promotionStockQuantity, purchaseQuantity) / (this.promotion.getBuy() + this.promotion.getGet())) * this.promotion.getGet();
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
        if(this.promotion == null) return 0;
        return (Math.min(this.promotionStockQuantity, purchaseQuantity)  / (this.promotion.getBuy() + this.promotion.getGet())) * (this.promotion.getBuy() + this.promotion.getGet());
    }
}
