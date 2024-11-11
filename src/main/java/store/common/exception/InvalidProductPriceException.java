package store.common.exception;

public class InvalidProductPriceException extends ConvenienceStoreException {
    public InvalidProductPriceException(String invalidProductPrice) {
        super(ErrorMessage.INVALID_PRODUCT_PRICE, "(제품 가격: " + invalidProductPrice + ")");
    }

    public InvalidProductPriceException(String invalidProductPrice, Exception cause) {
        super(ErrorMessage.INVALID_PRODUCT_PRICE, "(제품 가격: " + invalidProductPrice + ")", cause);
    }
}
