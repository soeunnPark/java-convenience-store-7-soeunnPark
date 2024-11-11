package store.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.receipt.Receipt;

public record ReceiptResponse(
        List<OrderResponse> ordersResponse,
        List<PromotionGiveawayResponse> promotionGiveawaysResponse,
        int totalPurchaseAmount,
        int totalPurchaseCount,
        int promotionDiscount,
        int membershipDiscount,
        int payment
) {

    public static ReceiptResponse from(Order order, Receipt receipt) {
        return new ReceiptResponse(OrderResponse.from(order),
                PromotionGiveawayResponse.from(receipt.getPromotionGiveaway()),
                receipt.getTotalPurchaseAmount(),
                receipt.getTotalPurchaseCount(),
                receipt.getPromotionDiscount(),
                receipt.getMembershipDiscount(),
                receipt.getPayment()
        );
    }

    public record OrderResponse(
            String productName,
            int purchaseQuantity,
            int totalPrice
    ) {
        public static List<OrderResponse> from(Order order) {
            List<OrderResponse> ordersResponse = new ArrayList<>();
            for (Product product : order.getOrder().keySet()) {
                ordersResponse.add(new OrderResponse(product.getName(),
                        order.getOrder().get(product),
                        product.getPrice() * order.getOrder().get(product)
                ));
            }
            return ordersResponse;
        }
    }

    public record PromotionGiveawayResponse(
            String productName,
            int freeGiveaway
    ) {
        public static List<PromotionGiveawayResponse> from(Map<Product, Integer> promotionGiveaway) {
            List<PromotionGiveawayResponse> promotionGiveawayResponse = new ArrayList<>();
            for (Product product : promotionGiveaway.keySet()) {
                promotionGiveawayResponse.add(new PromotionGiveawayResponse(
                        product.getName(),
                        promotionGiveaway.get(product)
                ));
            }
            return promotionGiveawayResponse;
        }
    }
}
