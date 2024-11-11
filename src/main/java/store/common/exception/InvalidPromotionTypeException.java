package store.common.exception;

public class InvalidPromotionTypeException extends ConvenienceStoreException {
    public InvalidPromotionTypeException(String promotion) {
        super(ErrorMessage.INVALID_PROMOTION_TYPE, "(프로모션: " + promotion + ")");
    }

    public InvalidPromotionTypeException(String promotion, Exception e) {
        super(ErrorMessage.INVALID_PROMOTION_TYPE, "(프로모션: " + promotion + ")", e);
    }
}
