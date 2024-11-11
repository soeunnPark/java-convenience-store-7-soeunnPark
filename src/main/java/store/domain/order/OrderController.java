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
import store.interfaces.AdditionalPurchaseResponse;
import store.interfaces.InputHandler;
import store.interfaces.OrderRequest;
import store.interfaces.OutputHandler;
import store.interfaces.ProductInventoryResponse;
import store.interfaces.ProductRequest;
import store.interfaces.PromotionNonApplicablePurchaseResponse;
import store.interfaces.PromotionRequest;
import store.interfaces.ReceiptResponse;

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
            boolean isMembership = askForMembership();
            outputHandler.printNewLine();
            purchase(isMembership, order, productInventories);
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
        while(true) {
            try {
                return inputHandler.readMembership();
            } catch (ConvenienceStoreException e) {
                System.out.println(e.getErrorMessageForClient());
            }
        }
    }

    private List<ProductInventory> makeStore() {
        List<ProductRequest> productRequests = inputHandler.readProducts();
        List<PromotionRequest> promotionRequests = inputHandler.readPromotions();
        productService.createProducts(productRequests);
        promotionService.createPromotion(promotionRequests);
        return productInventoryService.createProductInventory(productRequests);
    }

    private Order getOrder() {
        Map<Product, Integer> orders;
        Order order;
        while (true) {
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

    private void modifyOrder(Order order) {
        for (Product product : order.getOrder().keySet()) {
            addProductForPromotion(order, product, order.getOrder().get(product));
            excludeNonPromotionProduct(order, product, order.getOrder().get(product));
        }
    }

    private void excludeNonPromotionProduct(Order order, Product product, Integer purchaseQuantity) {
        int promotionNonApplicablePurchaseQuantity = orderService.getPromotionNonApplicablePurchaseQuantity(product, purchaseQuantity);
        if (promotionNonApplicablePurchaseQuantity > 0) {
            while (true) {
                try {
                    if(!inputHandler.askExcludeNonPromotion(PromotionNonApplicablePurchaseResponse.of(product, promotionNonApplicablePurchaseQuantity))) {
                        order.excludeNonPromotionCount(product, promotionNonApplicablePurchaseQuantity);
                    }
                    break;
                } catch (ConvenienceStoreException e) {
                    System.out.println(e.getErrorMessageForClient());
                }
            }
            outputHandler.printNewLine();
        }
    }

    private void addProductForPromotion(Order order, Product product, Integer purchaseQuantity) {
        int recommendAdditionalPurchaseQuantity = orderService.recommendAdditionalPurchase(product, purchaseQuantity);
        if (recommendAdditionalPurchaseQuantity > 0) {
            while (true) {
                try {
                    if(inputHandler.askAdditionalPurchaseForPromotion(
                            AdditionalPurchaseResponse.of(product, recommendAdditionalPurchaseQuantity))) {
                        order.addPurchaseCount(product, recommendAdditionalPurchaseQuantity);
                    }
                    break;
                } catch (ConvenienceStoreException e) {
                    System.out.println(e.getErrorMessageForClient());
                }
            }
            outputHandler.printNewLine();
        }
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
