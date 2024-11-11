package store.domain.promotion;

import java.util.List;
import store.interfaces.promotion.PromotionRequest;

public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public void createPromotion(List<PromotionRequest> promotionRequests) {
        promotionRequests.stream()
                .map(PromotionRequest::toPromotion)
                .forEach(promotionRepository::save);
    }
}
