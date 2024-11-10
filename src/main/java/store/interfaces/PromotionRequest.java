package store.interfaces;

import java.time.LocalDate;
import store.domain.promotion.PromotionType;

public record PromotionRequest(
        PromotionType promotionType,
        String promotionName,
        int buy,
        int get,
        LocalDate startDate,
        LocalDate endDate
) {
    public static PromotionRequest of(String name, String buy, String get, String startDate, String endDate) {
        return new PromotionRequest(PromotionType.from(name), name, Integer.parseInt(buy), Integer.parseInt(get), LocalDate.parse(startDate), LocalDate.parse(endDate));
    }
}
