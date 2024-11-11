package store.domain.receipt;

import java.util.HashMap;
import java.util.Map;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.StoreInventory;
import store.domain.order.Order;
import store.domain.product.Product;

public class ReceiptService {

    public Receipt makeReceipt(boolean isMembership, Order order, StoreInventory storeInventory) {
        Map<Product, Integer> promotionGiveaway = getPromotionGiveaway(order, storeInventory);
        int membershipDiscount = 0;
        if(isMembership) {
            membershipDiscount = calculateMembershipDiscount(order, storeInventory);
        }
        return new Receipt(order, promotionGiveaway, membershipDiscount);
    }

    private Map<Product, Integer> getPromotionGiveaway(Order order, StoreInventory storeInventory) {
        Map<Product, Integer> promotionGiveaway = new HashMap<>();
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            int promotionGiveawayCount = productInventory.getPromotionGiveawayCount(order.getOrder().get(product));
            if (promotionGiveawayCount > 0) {
                promotionGiveaway.put(product, promotionGiveawayCount);
            }
        }
        return promotionGiveaway;
    }

    private int calculateMembershipDiscount(Order order, StoreInventory storeInventory) {
        int purchaseAmountForMembershipDiscount = 0;
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            if (productInventory.hasPromotion()) {
                int promotionNonApplicablePurchaseQuantity = productInventory.getPromotionNonApplicablePurchaseQuantity(
                        order.getOrder().get(product));
                purchaseAmountForMembershipDiscount += promotionNonApplicablePurchaseQuantity * product.getPrice();
                continue;
            }
            purchaseAmountForMembershipDiscount += order.getOrder().get(product) * product.getPrice();
        }
        return (int) (purchaseAmountForMembershipDiscount * 0.3);
    }
}
