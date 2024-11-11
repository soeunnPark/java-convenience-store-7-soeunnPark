package store.interfaces.order;

import store.domain.product.Product;

public record AdditionalPurchaseResponse(
        String productName,
        int recommendedAdditionalPurchaseQuantity
) {
    public static AdditionalPurchaseResponse of(Product product, int recommendedAdditionalPurchaseQuantity) {
        return new AdditionalPurchaseResponse(product.getName(), recommendedAdditionalPurchaseQuantity);
    }
}
