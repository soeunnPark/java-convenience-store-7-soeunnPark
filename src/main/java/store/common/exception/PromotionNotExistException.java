package store.common.exception;

import store.domain.promotion.PromotionType;

public class PromotionNotExistException extends ConvenienceStoreException{
    public PromotionNotExistException(String promotionName) {
        super(ErrorMessage.PROMOTION_NOT_EXIST, "(프로모션 이름: " + promotionName + ")");
    }

    public PromotionNotExistException(String promotionName, Exception cause) {
        super(ErrorMessage.PROMOTION_NOT_EXIST, "(프로모션 이름: " + promotionName + ")", cause);
    }
}
