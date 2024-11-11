package store.domain.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.common.exception.ConvenienceStoreException;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryService;
import store.domain.product.Product;
import store.domain.product.ProductService;
import store.domain.promotion.PromotionService;
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

    private final ProductInventoryService productInventoryService;

    private final ReceiptService receiptService;
    private final ProductService productService;
    private final PromotionService promotionService;


    public OrderController(InputHandler inputHandler, OutputHandler outputHandler, ReceiptService receiptService,
                           ProductInventoryService productInventoryService, ProductService productService,
                           PromotionService promotionService) {
        this.inputHandler = inputHandler;
        this.receiptService = receiptService;
        this.outputHandler = outputHandler;
        this.productInventoryService = productInventoryService;
        this.productService = productService;
        this.promotionService = promotionService;
    }

    public void start() {
        List<ProductInventory> productInventoryRepository = makeStore();
        do {
            printStore(productInventoryRepository);
            Order order = getOrder(productInventoryRepository);
            modifyOrder(order, productInventoryRepository);
            boolean isMembership = askForMembership();
            outputHandler.printNewLine();
            purchase(isMembership, order, productInventoryRepository);
        } while (inputHandler.askContinue());
        inputHandler.closeConsole();
    }

    private void printStore(List<ProductInventory> productInventories) {
        List<ProductInventoryResponse> productsInventoryResponse = productInventories.stream()
                .map(ProductInventoryResponse::of)
                .toList();
        outputHandler.printStoreInventory(productsInventoryResponse);
    }

    private boolean askForMembership() {
        return inputHandler.readMembership().equals("Y");
    }

    private List<ProductInventory> makeStore() {
        List<ProductRequest> productRequests = inputHandler.readProducts();
        List<PromotionRequest> promotionRequests = inputHandler.readPromotions();
        productService.createProducts(productRequests);
        promotionService.createPromotion(promotionRequests);
        return productInventoryService.createProductInventory(productRequests);
    }

    private Order getOrder(List<ProductInventory> productInventories) {
        Map<Product, Integer> orders;
        Order order;
        while(true) {
            try {
                List<OrderRequest> orderRequests = inputHandler.readOrder();
                orders = orderRequests.stream()
                        .collect(Collectors.toMap(
                                orderRequest -> productService.findProduct(orderRequest.productName()),
                                OrderRequest::purchaseQuantity
                        ));
                order = new Order(orders);
                productInventoryService.validateStoreInventory(order);
                break;
            } catch (ConvenienceStoreException e) {
                System.out.println(e.getErrorMessageForClient());
            }
        }
        outputHandler.printNewLine();
        return order;
    }

    private void modifyOrder(Order order, List<ProductInventory> productInventories) {
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = productInventoryService.findProductInventory(product.getName());
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

    private void purchase(boolean isMembership, Order order, List<ProductInventory> productInventories) {
        Receipt receipt = receiptService.makeReceipt(isMembership, order, productInventories);
        for (Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = productInventoryService.findProductInventory(product.getName());
            productInventory.purchase(order.getOrder().get(product));
        }
        outputHandler.printReceipt(ReceiptResponse.from(order, receipt));
        outputHandler.printNewLine();
    }
}
