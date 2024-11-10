package store.interfaces;

import java.util.List;

public class OutputHandler {

    public void printStoreInventory(List<ProductInventoryResponse> productsInventoryResponse) {
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.\n");

        for (ProductInventoryResponse productInventoryResponse : productsInventoryResponse) {
            if(productInventoryResponse.hasPromotion()){
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d",productInventoryResponse.productPrice()) + "원 " +
                        (productInventoryResponse.promotionStockQuantity() == 0 ? "재고 없음 " :
                                productInventoryResponse.promotionStockQuantity() + "개 ") +
                        productInventoryResponse.promotionName());
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d", productInventoryResponse.productPrice()) + "원 " +
                        (productInventoryResponse.stockQuantity() == 0 ? "재고 없음" :
                                productInventoryResponse.stockQuantity() + "개"));
            }
            else {
                System.out.println("- " + productInventoryResponse.productName() + " " +
                        String.format("%,d", productInventoryResponse.productPrice())+ "원 " +
                        (productInventoryResponse.stockQuantity() == 0 ? "재고 없음" :
                                productInventoryResponse.stockQuantity() + "개"));
            }
        }
        System.out.println();
    }

    public void printReceipt(ReceiptResponse receiptResponse) {
        System.out.println("==============W 편의점================");
        System.out.printf("%-10s %5s %10s%n", "상품명", "수량", "금액");

        // 주문 항목 출력
        for (ReceiptResponse.OrderResponse order : receiptResponse.ordersResponse()) {
            System.out.printf("%-10s %5d %,10d%n", order.productName(), order.purchaseQuantity(), order.totalPrice());
        }

        System.out.println("=============증\t정==============");

        // 증정품 항목 출력
        for (ReceiptResponse.PromotionGiveawayResponse giveaway : receiptResponse.promotionGiveawaysResponse()) {
            System.out.printf("%-10s %5d%n", giveaway.productName(), giveaway.freeGiveaway());
        }

        System.out.println("====================================");

        // 총 구매액, 행사 할인, 멤버십 할인, 최종 결제 금액 출력
        System.out.printf("%-10s %10s %,10d%n", "총구매액", "", receiptResponse.totalPurchaseAmount());
        System.out.printf("%-10s %10s %,10d%n", "행사할인", "", -receiptResponse.promotionDiscount());
        System.out.printf("%-10s %10s %,10d%n", "멤버십할인", "", -receiptResponse.membershipDiscount());
        System.out.printf("%-10s %10s %,10d%n", "내실돈", "", receiptResponse.payment());
    }

}
