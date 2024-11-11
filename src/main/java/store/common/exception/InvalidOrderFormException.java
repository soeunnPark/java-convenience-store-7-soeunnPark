package store.common.exception;

public class InvalidOrderFormException extends ConvenienceStoreException {
    public InvalidOrderFormException(String invalidInput) {
        super(ErrorMessage.INVALID_ORDER_FORM, "(입력값: " + invalidInput + ")");
    }

    public InvalidOrderFormException(String invalidInput, Exception cause) {
        super(ErrorMessage.INVALID_ORDER_FORM, "(입력값: " + invalidInput + ")", cause);
    }
}
