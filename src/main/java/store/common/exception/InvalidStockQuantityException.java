package store.common.exception;

public class InvalidStockQuantityException extends ConvenienceStoreException {
    public InvalidStockQuantityException(String invalidStockQuantity) {
        super(ErrorMessage.INVALID_STOCK_QUANTITY, "(재고 수량: " + invalidStockQuantity);
    }

    public InvalidStockQuantityException(String invalidStockQuantity, Exception cause) {
        super(ErrorMessage.INVALID_STOCK_QUANTITY, "(재고 수량: " + invalidStockQuantity, cause);
    }
}
