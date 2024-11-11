package store.domain.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

class ProductInventoryTest {

    @AfterEach
    void resetSystemIn() {
        System.setIn(System.in);
    }

    @DisplayName("2+1 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 때, 추가로 받을 수 있는 상품 개수를 구한다.")
    @ParameterizedTest
    @CsvSource({
            "2, 1",
            "3, 0",
            "4, 0",
            "5, 1"
    })
    void recommendAdditionalPurchase(int purchaseQuantity, int recommendedAdditionalPurchaseQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion);
        productInventory.updateStockQuantity(10);
        productInventory.updatePromotionStockQuantity(10);
        assertThat(productInventory.recommendAdditionalPurchase(purchaseQuantity)).isEqualTo(
                recommendedAdditionalPurchaseQuantity);
    }

    @DisplayName("1+1 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 때, 추가로 받을 수 있는 상품 개수를 구한다.")
    @ParameterizedTest
    @CsvSource({
            "2, 0",
            "4, 0",
            "5, 1"
    })
    void recommendAdditionalPurchase_whenBuyCountIsOne(int purchaseQuantity,
                                                       int recommendedAdditionalPurchaseQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("탄산1+1", 1, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion);
        productInventory.updateStockQuantity(10);
        productInventory.updatePromotionStockQuantity(10);
        assertThat(productInventory.recommendAdditionalPurchase(purchaseQuantity)).isEqualTo(
                recommendedAdditionalPurchaseQuantity);
    }

    @DisplayName("프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 혜택 적용이 안되는 일부 수량을 구한다.")
    @ParameterizedTest
    @CsvSource({
            "1, 탄산1+1, 2, 5, 3",
            "1, 탄산1+1, 2, 4, 2",
            "1, 탄산1+1, 3, 4, 2",
            "1, 탄산1+1, 3, 5, 3",
            "2, 탄산2+1, 3, 5, 2",
            "2, 탄산2+1, 3, 6, 3",
            "2, 탄산2+1, 3, 7, 4",
            "2, 탄산2+1, 4, 8, 5",
            "2, 탄산2+1, 7, 9, 3"
    })
    void getPromotionNonApplicablePurchaseQuantity(int buyQuantityForPromotion, String promotionName,
                                                   int promotionStockQuantity, int purchaseQuantity,
                                                   int promotionNonApplicablePurchaseQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion(promotionName, buyQuantityForPromotion, 1, LocalDate.of(2024, 11, 9),
                LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion);
        productInventory.updateStockQuantity(10);
        productInventory.updatePromotionStockQuantity(promotionStockQuantity);
        assertThat(productInventory.getPromotionNonApplicablePurchaseQuantity(purchaseQuantity)).isEqualTo(
                promotionNonApplicablePurchaseQuantity);
    }

    @DisplayName("프로모션 혜택을 받을 증정품의 개수를 구한다.")
    @ParameterizedTest
    @CsvSource({
            "2, 10, 2, 0",
            "2, 10, 3, 1",
            "2, 10, 4, 1",
            "2, 10, 5, 1",
            "2, 10, 6, 2",
            "2, 3, 5, 1",
            "2, 3, 6, 1",
            "2, 3, 7, 1",
            "2, 4, 8, 1",
            "2, 7, 9, 2"
    })
    void getPromotionGiveawayCount(int buyQuantityForPromotion, int promotionStockQuantity, int purchaseQuantity,
                                   int promotionGiveawayCount) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("탄산2+1", buyQuantityForPromotion, 1, LocalDate.of(2024, 11, 9),
                LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion);
        productInventory.updateStockQuantity(10);
        productInventory.updatePromotionStockQuantity(promotionStockQuantity);
        assertThat(productInventory.getPromotionGiveawayCount(purchaseQuantity)).isEqualTo(promotionGiveawayCount);
    }

    @DisplayName("구매를 진행한다.")
    @ParameterizedTest
    @CsvSource({
            "3, 10, 10, 7, 17",
            "5, 3, 8, 0, 8",
            "8, 7, 9, 0, 9",
            "11, 3, 2, 0, 2",
            "12, 8, 6, 0, 6"
    })
    void purchase(int purchaseQuantity, int promotionStockQuantity, int expectedStockQuantity,
                  int expectedPromotionStockQuantity, int expectedTotalStockQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion("탄산2+1", 2, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion);
        productInventory.updateStockQuantity(10);
        productInventory.updatePromotionStockQuantity(promotionStockQuantity);
        productInventory.purchase(purchaseQuantity);
        assertThat(productInventory.getStockQuantity()).isEqualTo(expectedStockQuantity);
        assertThat(productInventory.getPromotionStockQuantity()).isEqualTo(expectedPromotionStockQuantity);
        assertThat(productInventory.getTotalStockQuantity()).isEqualTo(expectedTotalStockQuantity);
    }
}
