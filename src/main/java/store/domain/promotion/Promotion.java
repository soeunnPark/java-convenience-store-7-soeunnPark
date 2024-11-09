package store.domain.promotion;

import java.time.LocalDate;

public class Promotion {
    private final PromotionType promotionType;
    private final int buy;
    private final int get;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(PromotionType promotionType, int buy, int get, LocalDate startDate, LocalDate endDate) {
        this.promotionType = promotionType;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean isApplicable(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
