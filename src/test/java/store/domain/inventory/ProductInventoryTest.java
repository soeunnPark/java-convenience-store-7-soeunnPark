package store.domain.inventory;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import store.domain.product.Product;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionType;

class ProductInventoryTest {

    @DisplayName("프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 때, 추가로 받을 수 있는 상품 개수를 구한다.")
    @ParameterizedTest
    @CsvSource( {
            "2, 1",
            "4, 2",
            "6, 3"
    })
    void recommendAdditionalPurchase(int purchaseQuantity, int recommendedAdditionalPurchaseQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion(PromotionType.BUY_N_GET_ONE_FREE, 2, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion, 10, 10);
        assertThat(productInventory.recommendAdditionalPurchase(purchaseQuantity)).isEqualTo(recommendedAdditionalPurchaseQuantity);
    }

    @DisplayName("프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았지만 프로모션 재고가 조금 부족할 때, 추가로 받을 수 있는 상품 개수를 구한다.")
    @ParameterizedTest
    @CsvSource( {
            "7, 6, 1",
            "8, 6, 2",
            "5, 4, 1"
    })
    void recommendAdditionalPurchase_whenStockQuantityNotAvailable(int promotionStockQuantity, int purchaseQuantity, int recommendedAdditionalPurchaseQuantity) {
        Product product = new Product("콜라", 1000);
        Promotion promotion = new Promotion(PromotionType.BUY_N_GET_ONE_FREE, 2, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory productInventory = new ProductInventory(product, promotion, 10, promotionStockQuantity);
        assertThat(productInventory.recommendAdditionalPurchase(purchaseQuantity)).isEqualTo(recommendedAdditionalPurchaseQuantity);
    }
}
