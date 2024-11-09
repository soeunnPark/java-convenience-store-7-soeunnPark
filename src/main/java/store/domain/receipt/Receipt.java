package store.domain.receipt;

import java.util.Map;
import store.domain.order.Order;
import store.domain.product.Product;

public class Receipt {
    private final Order order;
    private final int totalPurchaseAmount;
    private final Map<Product, Integer> promotionGiveaway;

    public Receipt(Order order, Map<Product, Integer> promotionGiveaway) {
        this.order = order;
        this.totalPurchaseAmount = calculateTotalPurchaseAmount();
        this.promotionGiveaway = promotionGiveaway;
    }

    private int calculateTotalPurchaseAmount(){
        int sum = 0;
        for(Product product : order.getOrder().keySet()) {
            sum += product.getPrice() * order.getOrder().get(product);
        }
        return sum;
    }
}
