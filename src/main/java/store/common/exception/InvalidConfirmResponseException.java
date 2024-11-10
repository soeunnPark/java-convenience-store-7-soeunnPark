package store.common.exception;

public class InvalidConfirmResponseException extends ConvenienceStoreException{

    public InvalidConfirmResponseException(String input) {
        super(ErrorMessage.INVALID_CONFIRM_RESPONSE, "(입력값: " + input + ")");
    }

    public InvalidConfirmResponseException(String input, Exception cause) {
        super(ErrorMessage.INVALID_CONFIRM_RESPONSE, "(입력값: " + input + ")", cause);
    }
}
