package store.common.exception;

public class ConvenienceStoreException extends IllegalArgumentException {

    private static final String errorSuffixForClient = " 다시 입력해 주세요.";

    private final String errorMessageForClient;
    private final String errorMessageForLog;

    public ConvenienceStoreException(ErrorMessage errorMessage, String errorDetails) {
        super(errorMessage.getMessage() + errorDetails);
        this.errorMessageForClient = errorMessage.getMessage() + errorSuffixForClient;
        this.errorMessageForLog = errorMessage.getMessage() + errorDetails;
    }

    public ConvenienceStoreException(ErrorMessage errorMessage, String errorDetails, Exception cause) {
        super(errorMessage.getMessage() + errorDetails, cause);
        this.errorMessageForClient = errorMessage.getMessage() + errorSuffixForClient;
        this.errorMessageForLog = errorMessage.getMessage() + errorDetails;
    }

    public String getErrorMessageForClient() {
        return errorMessageForClient;
    }
}
