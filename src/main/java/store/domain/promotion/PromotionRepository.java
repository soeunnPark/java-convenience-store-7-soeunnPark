package store.domain.promotion;

import java.util.ArrayList;
import java.util.List;
import store.common.exception.PromotionNotExistException;

public class PromotionRepository {
    private final List<Promotion> promotions;

    public PromotionRepository() {
        this.promotions = new ArrayList<>();
    }

    public void save(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public Promotion findPromotion(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(promotionName))
                .findAny()
                .orElseThrow(() -> new PromotionNotExistException(promotionName));
    }
}
