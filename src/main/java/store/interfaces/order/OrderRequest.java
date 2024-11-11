package store.interfaces.order;

import store.common.exception.InvalidPurchaseQuantityException;

public record OrderRequest(
        String productName,
        int purchaseQuantity
) {

    public static OrderRequest of(String productName, String purchaseQuantity) {
        try {
            Integer.parseInt(purchaseQuantity);
        } catch (NumberFormatException e) {
            throw new InvalidPurchaseQuantityException(purchaseQuantity);
        }
        return new OrderRequest(productName, Integer.parseInt(purchaseQuantity));
    }
}
