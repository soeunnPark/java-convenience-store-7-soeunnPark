package store.domain.receipt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryRepository;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.promotion.Promotion;

class ReceiptServiceTest {

    @AfterEach
    void resetSystemIn() {
        System.setIn(System.in);
    }

    @DisplayName("영수증을 발행하는 테스트")
    @Test
    void makeReceipt() {
        Product cola = new Product("콜라", 1000);
        Promotion promotion = new Promotion(
                "탄산2+1", 2, 1, LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 15));
        ProductInventory colaInventory = new ProductInventory(cola, promotion);
        colaInventory.updateStockQuantity(10);
        colaInventory.updatePromotionStockQuantity(10);
        Product energyBar = new Product("에너지바", 2000);
        ProductInventory energyBarInventory = new ProductInventory(energyBar, null);
        energyBarInventory.updateStockQuantity(5);
        energyBarInventory.updatePromotionStockQuantity(0);
        ProductInventoryRepository productInventoryRepository = new ProductInventoryRepository();
        productInventoryRepository.saveProductInventory(colaInventory);
        productInventoryRepository.saveProductInventory(energyBarInventory);
        Order order = new Order(Map.of(cola, 3, energyBar, 5));

        ReceiptService receiptService = new ReceiptService(productInventoryRepository);
        Receipt receipt = receiptService.makeReceipt(true, order);

        assertAll(
                () -> assertThat(receipt.getTotalPurchaseAmount()).isEqualTo(13000),
                () -> assertThat(receipt.getPromotionDiscount()).isEqualTo(1000),
                () -> assertThat(receipt.getMembershipDiscount()).isEqualTo(3000),
                () -> assertThat(receipt.getPayment()).isEqualTo(9000)
        );
    }

}