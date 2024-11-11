package store.domain.receipt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryRepository;
import store.domain.order.Order;
import store.domain.product.Product;

public class ReceiptService {

    private final ProductInventoryRepository productInventoryRepository;

    public ReceiptService(ProductInventoryRepository productInventoryRepository) {
        this.productInventoryRepository = productInventoryRepository;
    }

    public Receipt makeReceipt(boolean isMembership, Order order, List<ProductInventory> productInventories) {
        Map<Product, Integer> promotionGiveaway = getPromotionGiveaway(order, productInventories);
        int membershipDiscount = 0;
        if(isMembership) {
            membershipDiscount = calculateMembershipDiscount(order, productInventories);
        }
        return new Receipt(order, promotionGiveaway, membershipDiscount);
    }

    private Map<Product, Integer> getPromotionGiveaway(Order order, List<ProductInventory> productInventories) {
        Map<Product, Integer> promotionGiveaway = new HashMap<>();
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
            int promotionGiveawayCount = productInventory.getPromotionGiveawayCount(order.getOrder().get(product));
            if (promotionGiveawayCount > 0) {
                promotionGiveaway.put(product, promotionGiveawayCount);
            }
        }
        return promotionGiveaway;
    }

    private int calculateMembershipDiscount(Order order, List<ProductInventory> productInventories) {
        int purchaseAmountForMembershipDiscount = 0;
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
            if (productInventory.hasPromotion()) {
                int promotionNonApplicablePurchaseQuantity = productInventory.getPromotionNonApplicablePurchaseQuantity(
                        order.getOrder().get(product));
                purchaseAmountForMembershipDiscount += promotionNonApplicablePurchaseQuantity * product.getPrice();
                continue;
            }
            purchaseAmountForMembershipDiscount += order.getOrder().get(product) * product.getPrice();
        }
        return Math.min(8000, (int) (purchaseAmountForMembershipDiscount * 0.3));
    }
}
