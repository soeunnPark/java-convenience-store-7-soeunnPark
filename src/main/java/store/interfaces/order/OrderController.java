package store.interfaces.order;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import store.common.exception.ConvenienceStoreException;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryService;
import store.domain.order.Order;
import store.domain.order.OrderService;
import store.domain.product.Product;
import store.domain.product.ProductService;
import store.domain.promotion.PromotionService;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptService;
import store.interfaces.input.InputHandler;
import store.interfaces.output.OutputHandler;
import store.interfaces.product.ProductInventoryResponse;
import store.interfaces.product.ProductRequest;
import store.interfaces.promotion.PromotionNonApplicablePurchaseResponse;
import store.interfaces.promotion.PromotionRequest;
import store.interfaces.receipt.ReceiptResponse;

public class OrderController {

    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    private final ProductInventoryService productInventoryService;
    private final ReceiptService receiptService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final OrderService orderService;

    public OrderController(InputHandler inputHandler, OutputHandler outputHandler, ReceiptService receiptService,
                           ProductInventoryService productInventoryService, ProductService productService,
                           PromotionService promotionService, OrderService orderService) {
        this.inputHandler = inputHandler;
        this.receiptService = receiptService;
        this.outputHandler = outputHandler;
        this.productInventoryService = productInventoryService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.orderService = orderService;
    }

    public void start() {
        List<ProductInventory> productInventories = makeStore();
        do {
            printStore(productInventories);
            Order order = getOrder();
            modifyOrder(order);
            purchase(order);
        } while (inputHandler.askContinue());
        inputHandler.closeConsole();
    }

    private List<ProductInventory> makeStore() {
        List<ProductRequest> productRequests = inputHandler.readProducts();
        List<PromotionRequest> promotionRequests = inputHandler.readPromotions();
        productService.createProducts(productRequests);
        promotionService.createPromotion(promotionRequests);
        return productInventoryService.createProductInventory(productRequests);
    }

    private Order getOrder() {
        AtomicReference<Order> order = new AtomicReference<>();
        retryOnException(() -> {
            List<OrderRequest> orderRequests = inputHandler.readOrder();
            order.set(orderService.createOrder(orderRequests));
            productInventoryService.validateStoreInventory(order.get());
        });
        outputHandler.printNewLine();
        return order.get();
    }

    private void modifyOrder(Order order) {
        for (Product product : order.getOrder().keySet()) {
            addProductForPromotion(order, product, order.getOrder().get(product));
            excludeNonPromotionProduct(order, product, order.getOrder().get(product));
        }
    }

    private void excludeNonPromotionProduct(Order order, Product product, Integer purchaseQuantity) {
        int promotionNonApplicablePurchaseQuantity = orderService.getPromotionNonApplicablePurchaseQuantity(product,
                purchaseQuantity);
        if (promotionNonApplicablePurchaseQuantity > 0) {
            retryOnException(() -> {
                if (!inputHandler.askExcludeNonPromotion(PromotionNonApplicablePurchaseResponse.of(product,
                        promotionNonApplicablePurchaseQuantity))) {
                    order.excludeNonPromotionCount(product, promotionNonApplicablePurchaseQuantity);
                }
            });
            outputHandler.printNewLine();
        }
    }

    private void addProductForPromotion(Order order, Product product, Integer purchaseQuantity) {
        int recommendAdditionalPurchaseQuantity = orderService.recommendAdditionalPurchase(product, purchaseQuantity);
        if (recommendAdditionalPurchaseQuantity > 0) {
            retryOnException(() -> {
                if (inputHandler.askAdditionalPurchaseForPromotion(
                        AdditionalPurchaseResponse.of(product, recommendAdditionalPurchaseQuantity))) {
                    order.addPurchaseCount(product, recommendAdditionalPurchaseQuantity);
                }
            });
            outputHandler.printNewLine();
        }
    }

    private void printStore(List<ProductInventory> productInventories) {
        List<ProductInventoryResponse> productsInventoryResponse = productInventories.stream()
                .map(ProductInventoryResponse::of)
                .toList();
        outputHandler.printStoreInventory(productsInventoryResponse);
    }

    private void purchase(Order order) {
        boolean isMembership = askForMembership();
        Receipt receipt = receiptService.makeReceipt(isMembership, order);
        productInventoryService.updateStock(order);
        outputHandler.printReceipt(ReceiptResponse.from(order, receipt));
        outputHandler.printNewLine();
    }

    private boolean askForMembership() {
        AtomicBoolean isMembership = new AtomicBoolean(false);
        retryOnException(() -> {
            isMembership.set(inputHandler.readMembership());
            outputHandler.printNewLine();
        });
        return isMembership.get();
    }

    private void retryOnException(Runnable action) {
        while (true) {
            try {
                action.run();
                break;
            } catch (ConvenienceStoreException e) {
                System.out.println(e.getErrorMessageForClient());
            }
        }
    }
}
