package store.domain.promotion;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Promotion {
    private final PromotionType promotionType;
    private final String promotionName;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(PromotionType promotionType, String promotionName, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.promotionType = promotionType;
        this.promotionName = promotionName;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isApplicable(LocalDateTime date) {
        return !date.isBefore(startDate.atStartOfDay()) && !date.isAfter(endDate.atTime(23, 59, 59));
    }

    public String getPromotionName() {
        return promotionName;
    }

    public PromotionType getPromotionType() {
        return promotionType;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }
}
