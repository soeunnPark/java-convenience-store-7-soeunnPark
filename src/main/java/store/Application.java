package store;

import store.domain.inventory.ProductInventoryRepository;
import store.domain.inventory.ProductInventoryService;
import store.domain.order.OrderController;
import store.domain.order.OrderService;
import store.domain.product.ProductRepository;
import store.domain.product.ProductService;
import store.domain.promotion.PromotionRepository;
import store.domain.promotion.PromotionService;
import store.domain.receipt.ReceiptService;
import store.interfaces.input.InputHandler;
import store.interfaces.output.OutputHandler;

public class Application {
    public static void main(String[] args) {

        InputHandler inputHandler = new InputHandler();
        OutputHandler outputHandler = new OutputHandler();
        ProductInventoryRepository productInventoryRepository = new ProductInventoryRepository();
        ReceiptService receiptService = new ReceiptService(productInventoryRepository);
        ProductRepository productRepository = new ProductRepository();
        PromotionRepository promotionRepository = new PromotionRepository();

        ProductService productService = new ProductService(productRepository);
        PromotionService promotionService = new PromotionService(promotionRepository);
        OrderService orderService = new OrderService(productInventoryRepository, productRepository);
        ProductInventoryService productInventoryService = new ProductInventoryService(productRepository,
                promotionRepository, productInventoryRepository);
        OrderController orderController = new OrderController(inputHandler, outputHandler,
                receiptService, productInventoryService, productService, promotionService, orderService);
        orderController.start();
    }
}
