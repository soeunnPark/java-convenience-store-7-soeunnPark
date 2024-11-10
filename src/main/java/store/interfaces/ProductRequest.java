package store.interfaces;

import store.common.exception.InvalidProductPriceException;
import store.common.exception.InvalidStockQuantityException;
import store.domain.promotion.PromotionType;

public record ProductRequest(
        String productName,
        int productPrice,
        int stockQuantity,
        String promotionName,
        PromotionType promotionType
) {

    public static ProductRequest of(String name, String price, String quantity, String promotion) {
        int productPrice, stockQuantity;
        try {
            productPrice = Integer.parseInt(price);
        } catch (NumberFormatException e) {
            throw new InvalidProductPriceException(price);
        }
        try {
            stockQuantity = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new InvalidStockQuantityException(price);
        }
        if(promotion.equals("null")) {
            return new ProductRequest(name, productPrice, stockQuantity, null, null);
        }
        return new ProductRequest(name, productPrice, stockQuantity, promotion, PromotionType.from(promotion));
    }
}
