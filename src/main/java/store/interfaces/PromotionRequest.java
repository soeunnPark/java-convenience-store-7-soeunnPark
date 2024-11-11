package store.interfaces;

import java.time.LocalDate;
import store.domain.promotion.Promotion;

public record PromotionRequest(
        String promotionName,
        int buy,
        int get,
        LocalDate startDate,
        LocalDate endDate
) {
    public static PromotionRequest of(String name, String buy, String get, String startDate, String endDate) {
        return new PromotionRequest(name, Integer.parseInt(buy), Integer.parseInt(get), LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    public Promotion toPromotion() {
        return new Promotion(this.promotionName, this.buy, this.get, this.startDate, this.endDate);
    }
}
