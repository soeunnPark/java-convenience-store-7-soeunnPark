package store.domain.receipt;

import java.util.HashMap;
import java.util.Map;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.StoreInventory;
import store.domain.order.Order;
import store.domain.product.Product;

public class Receipt {
    private final Order order;
    private final int totalPurchaseAmount;
    private final Map<Product, Integer> promotionGiveaway;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int payment;

    public Receipt(Order order, StoreInventory storeInventory) {
        this.order = order;
        this.totalPurchaseAmount = calculateTotalPurchaseAmount();
        this.promotionGiveaway = getPromotionGiveaway(storeInventory);
        this.promotionDiscount = calculatePromotionDiscount(promotionGiveaway);
        this.membershipDiscount = calculateMembershipDiscount(storeInventory);
        this.payment = totalPurchaseAmount - promotionDiscount - membershipDiscount;
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

    private Map<Product, Integer> getPromotionGiveaway(StoreInventory storeInventory) {
        Map<Product, Integer> promotionGiveaway = new HashMap<>();
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            int promotionGiveawayCount = productInventory.getPromotionGiveawayCount(this.order.getOrder().get(product));
            if (promotionGiveawayCount > 0) {
                promotionGiveaway.put(product, promotionGiveawayCount);
            }
        }
        return promotionGiveaway;
    }

    private int calculateMembershipDiscount(StoreInventory storeInventory) {
        int purchaseAmountForMembershipDiscount = 0;
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            if (productInventory.hasPromotion()) {
                int promotionNonApplicablePurchaseQuantity = productInventory.getPromotionNonApplicablePurchaseQuantity(
                        this.order.getOrder().get(product));
                purchaseAmountForMembershipDiscount += promotionNonApplicablePurchaseQuantity * product.getPrice();
                continue;
            }
            purchaseAmountForMembershipDiscount += order.getOrder().get(product) * product.getPrice();
        }
        return (int) (purchaseAmountForMembershipDiscount * 0.3);
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
