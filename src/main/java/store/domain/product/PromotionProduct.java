package store.domain.product;

import store.domain.promotion.Promotion;

public class PromotionProduct extends Product{

    private final Promotion promotion;

    public PromotionProduct(String name, int price, int quantity, Promotion promotion) {
        super(name, price, quantity);
        this.promotion = promotion;
    }
}
