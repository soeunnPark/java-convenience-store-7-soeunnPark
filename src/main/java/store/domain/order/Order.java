package store.domain.order;

import java.util.Map;
import store.domain.product.Product;

public class Order {
    private final Map<Product, Integer> order;

    public Order(Map<Product, Integer> order) {
        this.order = order;
    }

    public Order addPurchaseCount(Product product, int recommendedAdditionalPurchaseQuantity) {
        order.put(product, order.get(product) + recommendedAdditionalPurchaseQuantity);
        return this;
    }

    public Order excludeNonPromotionCount(Product product, int promotionNonApplicablePurchaseQuantity) {
        order.put(product, order.get(product) - promotionNonApplicablePurchaseQuantity);
        return this;
    }

    public Map<Product, Integer> getOrder() {
        return order;
    }
}
