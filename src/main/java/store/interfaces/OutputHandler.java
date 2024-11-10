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
    }

}
