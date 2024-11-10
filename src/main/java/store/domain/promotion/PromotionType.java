package store.domain.promotion;

import java.util.EnumSet;
import store.common.exception.InvalidPromotionTypeException;

public enum PromotionType {
    MD_RECOMMEND("MD추천상품"),
    SPECIAL_DISCOUNT("반짝할인"),
    BUY_N_GET_ONE_FREE("N+1증정");

    private final String description;

    public static PromotionType from(String promotionName) {
        if(promotionName.matches("^.+[0-9]+\\+[0-9]+$")) {
            return BUY_N_GET_ONE_FREE;
        }
        return EnumSet.allOf(PromotionType.class).stream()
                .filter(promotionType -> promotionType.description.equals(promotionName))
                .findAny()
                .orElseThrow(() -> new InvalidPromotionTypeException(promotionName));
    }

    PromotionType(String description) {
        this.description = description;
    }
}
