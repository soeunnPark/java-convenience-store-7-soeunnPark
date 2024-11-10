package store.domain.receipt;

import java.util.Map;
import store.domain.order.Order;
import store.domain.product.Product;

public class Receipt {
    private final Order order;
    private final int totalPurchaseAmount;

    public int getTotalPurchaseCount() {
        return totalPurchaseCount;
    }

    private final int totalPurchaseCount;
    private final Map<Product, Integer> promotionGiveaway;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int payment;

    public Receipt(Order order, Map<Product, Integer> promotionGiveaway, int membershipDiscount) {
        this.order = order;
        this.totalPurchaseAmount = calculateTotalPurchaseAmount();
        this.totalPurchaseCount = calculateTotalPurchaseCount();
        this.promotionGiveaway = promotionGiveaway;
        this.promotionDiscount = calculatePromotionDiscount(promotionGiveaway);
        this.membershipDiscount = membershipDiscount;
        this.payment = totalPurchaseAmount - promotionDiscount - membershipDiscount;
    }

    private int calculateTotalPurchaseCount() {
        int sum = 0;
        for(int i : this.order.getOrder().values()) {
            sum += i;
        }
        return sum;
    }

    private int calculatePromotionDiscount(Map<Product, Integer> promotionGiveaway) {
        int promotionDiscount = 0;
        for (Product product : promotionGiveaway.keySet()) {
            promotionDiscount += product.getPrice() * promotionGiveaway.get(product);
        }
        return promotionDiscount;
    }

    private int calculateTotalPurchaseAmount() {
        int sum = 0;
        for (Product product : order.getOrder().keySet()) {
            sum += product.getPrice() * order.getOrder().get(product);
        }
        return sum;
    }

    public int getTotalPurchaseAmount() {
        return totalPurchaseAmount;
    }

    public Map<Product, Integer> getPromotionGiveaway() {
        return promotionGiveaway;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getPayment() {
        return payment;
    }
}
