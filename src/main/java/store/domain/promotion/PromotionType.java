package store.domain.promotion;

public enum PromotionType {
    MD_RECOMMEND("MD추천상품"),
    SPECIAL_DISCOUNT("반짝할인"),
    BUY_N_GET_ONE_FREE("N+1증정"),
    NONE("없음");

    private final String description;

    PromotionType(String description) {
        this.description = description;
    }
}
