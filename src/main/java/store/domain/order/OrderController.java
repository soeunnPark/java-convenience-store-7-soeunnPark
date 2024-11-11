package store.domain.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.common.exception.ConvenienceStoreException;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.StoreInventory;
import store.domain.inventory.StoreInventoryService;
import store.domain.product.Product;
import store.domain.product.ProductService;
import store.domain.receipt.Receipt;
import store.domain.receipt.ReceiptService;
import store.interfaces.AdditionalPurchase;
import store.interfaces.InputHandler;
import store.interfaces.OrderRequest;
import store.interfaces.OutputHandler;
import store.interfaces.ProductInventoryResponse;
import store.interfaces.ProductRequest;
import store.interfaces.PromotionNonApplicablePurchase;
import store.interfaces.PromotionRequest;
import store.interfaces.ReceiptResponse;

public class OrderController {

    private final InputHandler inputHandler;
    private final OutputHandler outputHandler;

    private final StoreInventoryService storeInventoryService;
    private final ReceiptService receiptService;
    private final ProductService productService;


    public OrderController(InputHandler inputHandler, OutputHandler outputHandler, ReceiptService receiptService,
                           StoreInventoryService storeInventoryService, ProductService productService) {
        this.inputHandler = inputHandler;
        this.receiptService = receiptService;
        this.outputHandler = outputHandler;
        this.storeInventoryService = storeInventoryService;
        this.productService = productService;
    }

    public void start() {
        StoreInventory storeInventory = makeStore();
        do {
            printStore(storeInventory);
            Order order = getOrder(storeInventory);
            modifyOrder(order, storeInventory);
            boolean isMembership = askForMembership();
            outputHandler.printNewLine();
            purchase(isMembership, order, storeInventory);
        } while (inputHandler.askContinue());
    }

    private void printStore(StoreInventory storeInventory) {
        List<ProductInventoryResponse> productsInventoryResponse = storeInventory.getStoreInventory().stream()
                .map(ProductInventoryResponse::of)
                .toList();
        outputHandler.printStoreInventory(productsInventoryResponse);
    }

    private boolean askForMembership() {
        return inputHandler.readMembership().equals("Y");
    }

    private StoreInventory makeStore() {
        List<ProductRequest> productRequests = inputHandler.readProducts();
        List<PromotionRequest> promotionRequests = inputHandler.readPromotions();

        return storeInventoryService.makeStoreInventory(productRequests, promotionRequests);
    }

    private Order getOrder(StoreInventory storeInventory) {
        Map<Product, Integer> orders;
        Order order = null;
        while(true) {
            try {
                List<OrderRequest> orderRequests = inputHandler.readOrder();
                orders = orderRequests.stream()
                        .collect(Collectors.toMap(
                                orderRequest -> productService.findProduct(orderRequest.productName()),
                                OrderRequest::purchaseQuantity
                        ));
                order = new Order(orders);
                storeInventoryService.validateStoreInventory(order, storeInventory);
                break;
            } catch (ConvenienceStoreException e) {
                System.out.println(e.getErrorMessageForClient());
            }
        }
        outputHandler.printNewLine();
        return order;
    }

    private void modifyOrder(Order order, StoreInventory storeInventory) {

        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            int recommendAdditionalPurchaseQuantity = productInventory.recommendAdditionalPurchase(
                    order.getOrder().get(product));
            if (recommendAdditionalPurchaseQuantity > 0) {
                var confirmAdditionalPurchase = inputHandler.askAdditionalPurchaseForPromotion(
                        AdditionalPurchase.Response.of(product, recommendAdditionalPurchaseQuantity));
                if (confirmAdditionalPurchase.addGiveaway().equals("Y")) {
                    order.addPurchaseCount(product, recommendAdditionalPurchaseQuantity);
                }
            }
            int promotionNonApplicablePurchaseQuantity = productInventory.getPromotionNonApplicablePurchaseQuantity(
                    order.getOrder().get(product));
            if (promotionNonApplicablePurchaseQuantity > 0) {
                var confirmExcludeNonPromotion = inputHandler.askExcludeNonPromotion(
                        PromotionNonApplicablePurchase.Response.of(product, promotionNonApplicablePurchaseQuantity));
                if (confirmExcludeNonPromotion.excludeNonPromotion().equals("N")) {
                    order.excludeNonPromotionCount(product, promotionNonApplicablePurchaseQuantity);
                }
            }
        }
        outputHandler.printNewLine();
    }

    private void purchase(boolean isMembership, Order order, StoreInventory storeInventory) {
        Receipt receipt = receiptService.makeReceipt(isMembership, order, storeInventory);
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            productInventory.purchase(order.getOrder().get(product));
        }
        outputHandler.printReceipt(ReceiptResponse.from(order, receipt));
        outputHandler.printNewLine();
    }
}
