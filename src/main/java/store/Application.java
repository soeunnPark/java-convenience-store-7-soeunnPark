package store;

import store.domain.inventory.StoreInventoryService;
import store.domain.order.OrderController;
import store.domain.product.ProductRepository;
import store.domain.product.ProductService;
import store.domain.promotion.PromotionRepository;
import store.domain.promotion.PromotionService;
import store.domain.receipt.ReceiptService;
import store.interfaces.InputHandler;
import store.interfaces.OutputHandler;

public class Application {
    public static void main(String[] args) {

        InputHandler inputHandler = new InputHandler();
        OutputHandler outputHandler = new OutputHandler();
        ReceiptService receiptService= new ReceiptService();
        ProductRepository productRepository = new ProductRepository();
        PromotionRepository promotionRepository = new PromotionRepository();

        ProductService productService = new ProductService(productRepository);
        PromotionService promotionService = new PromotionService(promotionRepository);
        StoreInventoryService storeInventoryService = new StoreInventoryService(productRepository, promotionRepository);
        OrderController orderController = new OrderController(inputHandler, outputHandler,
                receiptService, storeInventoryService, productService, promotionService);
        orderController.start();
    }
}
