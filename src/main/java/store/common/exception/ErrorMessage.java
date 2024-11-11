package store.common.exception;

public enum ErrorMessage {

    PURCHASE_QUANTITY_MUST_BE_SMALLER_THAN_STOCK_QUANTITY("재고 수량을 초과하여 구매할 수 없습니다."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다."),
    INVALID_CONFIRM_RESPONSE("잘못된 입력입니다."),
    INVALID_FILE_NAME("존재하지 않는 파일명입니다."),
    INVALID_PROMOTION_TYPE("잘못된 프로모션 타입입니다."),
    INVALID_STOCK_QUANTITY("잘못된 재고 수량입니다."),
    INVALID_PRODUCT_PRICE("잘못된 제품 가격입니다."),
    PROMOTION_NOT_EXIST("존재하지 않는 프로모션입니다."),
    INVALID_ORDER_FORM("올바르지 않은 형식으로 입력했습니다.");

    private static final String errorPrefix = "[ERROR] ";

    private final String message;

    ErrorMessage(String message) {
        this.message = errorPrefix + message;
    }

    public String getMessage() {
        return message;
    }
}
