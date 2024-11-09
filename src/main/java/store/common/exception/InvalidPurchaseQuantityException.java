package store.common.exception;

import store.domain.product.Product;

public class InvalidPurchaseQuantityException extends ConvenienceStoreException{

    public InvalidPurchaseQuantityException(Product product, int totalStockQuantity, int purchaseQuantity) {
        super(ErrorMessage.PURCHASE_QUANTITY_MUST_BE_SMALLER_THAN_STOCK_QUANTITY, "(상품 이름: " + product.getName() + " 전체 재고 수량: " + totalStockQuantity + " 구매 수량: " + purchaseQuantity + ")");
    }

    public InvalidPurchaseQuantityException(Product product, int totalStockQuantity, int purchaseQuantity, Exception cause) {
        super(ErrorMessage.PURCHASE_QUANTITY_MUST_BE_SMALLER_THAN_STOCK_QUANTITY, "(상품 이름: " + product.getName() + " 전체 재고 수량: " + totalStockQuantity + " 구매 수량: " + purchaseQuantity + ")", cause);
    }
}
