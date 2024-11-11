package store.interfaces.output;

import java.util.List;
import store.interfaces.product.ProductInventoryResponse;
import store.interfaces.receipt.ReceiptResponse;

public class OutputHandler {

    public void printStoreInventory(List<ProductInventoryResponse> productsInventoryResponse) {
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.\n");

        for (ProductInventoryResponse productInventoryResponse : productsInventoryResponse) {
            if (productInventoryResponse.hasPromotion()) {
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d", productInventoryResponse.productPrice()) + "원 " +
                        (productInventoryResponse.promotionStockQuantity() == 0 ? "재고 없음 " :
                                productInventoryResponse.promotionStockQuantity() + "개 ") +
                        productInventoryResponse.promotionName());
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d", productInventoryResponse.productPrice()) + "원 " +
                        (productInventoryResponse.stockQuantity() == 0 ? "재고 없음" :
                                productInventoryResponse.stockQuantity() + "개"));
            } else {
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d", productInventoryResponse.productPrice()) + "원 " +
                        (productInventoryResponse.stockQuantity() == 0 ? "재고 없음" :
                                productInventoryResponse.stockQuantity() + "개"));
            }
        }
        System.out.println();
    }

    public void printReceipt(ReceiptResponse receiptResponse) {
        System.out.println("==============W 편의점================");
        System.out.printf("%-19s%-9s%-7s%n", "상품명", "수량", "금액");

        for (ReceiptResponse.OrderResponse order : receiptResponse.ordersResponse()) {
            System.out.printf("%-19s%-9s%-7s%n", order.productName(), String.valueOf(order.purchaseQuantity()),
                    String.format("%,d", order.totalPrice()));
        }

        System.out.println("=============증      정===============");

        // 증정품 항목 출력
        for (ReceiptResponse.PromotionGiveawayResponse giveaway : receiptResponse.promotionGiveawaysResponse()) {
            System.out.printf("%-19s%-9s%n", giveaway.productName(), String.valueOf(giveaway.freeGiveaway()));
        }

        System.out.println("====================================");

        // 총 구매액, 행사 할인, 멤버십 할인, 최종 결제 금액 출력
        System.out.printf("%-19s%-9s%-7s%n", "총구매액", String.format("%,d", receiptResponse.totalPurchaseCount()),
                String.format("%,d", receiptResponse.totalPurchaseAmount()));

        // 할인 항목에서 0일 경우에도 항상 - 표시
        System.out.printf("%-28s%-7s%n", "행사할인", String.format("-%,d", Math.abs(receiptResponse.promotionDiscount())));
        System.out.printf("%-28s%-7s%n", "멤버십할인",
                String.format("-%,d", Math.abs(receiptResponse.membershipDiscount())));
        System.out.printf("%-28s%-7s%n", "내실돈", String.format("%,d", Math.abs(receiptResponse.payment())));
    }

    public void printNewLine() {
        System.out.println();
    }
}
