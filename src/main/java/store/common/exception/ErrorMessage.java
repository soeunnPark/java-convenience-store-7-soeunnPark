package store.common.exception;

public enum ErrorMessage {

    PURCHASE_QUANTITY_MUST_BE_SMALLER_THAN_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다.");

    private static final String errorPrefix = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = errorPrefix + message;
    }

    public String getMessage() {
        return message;
    }
}
