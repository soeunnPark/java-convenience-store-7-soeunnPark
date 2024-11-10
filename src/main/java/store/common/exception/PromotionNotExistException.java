package store.common.exception;

import store.domain.promotion.PromotionType;

public class PromotionNotExistException extends ConvenienceStoreException{
    public PromotionNotExistException(PromotionType promotionType) {
        super(ErrorMessage.PROMOTION_NOT_EXIST, "(프로모션 타입: " + promotionType + ")");
    }

    public PromotionNotExistException(PromotionType promotionType, Exception cause) {
        super(ErrorMessage.PROMOTION_NOT_EXIST, "(프로모션 타입: " + promotionType + ")", cause);
    }
}
