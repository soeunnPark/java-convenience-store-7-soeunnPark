package store.interfaces;

import store.domain.product.Product;

public record PromotionNonApplicablePurchaseResponse(
        String productName,
        int promotionNonApplicablePurchaseQuantity
) {
    public static PromotionNonApplicablePurchaseResponse of(Product product, int promotionNonApplicablePurchaseQuantity) {
        return new PromotionNonApplicablePurchaseResponse(product.getName(), promotionNonApplicablePurchaseQuantity);
    }
}

