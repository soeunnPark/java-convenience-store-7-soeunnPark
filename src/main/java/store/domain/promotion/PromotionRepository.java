package store.domain.promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PromotionRepository {
    private final List<Promotion> promotions;

    public PromotionRepository() {
        this.promotions = new ArrayList<>();
    }

    public void save(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public Optional<Promotion> findPromotion(String promotionName) {
        return promotions.stream()
                .filter(promotion -> promotion.getPromotionName().equals(promotionName))
                .findAny();
    }
}
