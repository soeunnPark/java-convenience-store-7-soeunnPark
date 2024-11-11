package store.domain.order;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.domain.inventory.ProductInventory;
import store.domain.inventory.ProductInventoryRepository;
import store.domain.product.Product;
import store.domain.product.ProductRepository;
import store.interfaces.OrderRequest;

public class OrderService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductRepository productRepository;

    public OrderService(ProductInventoryRepository productInventoryRepository, ProductRepository productRepository) {
        this.productInventoryRepository = productInventoryRepository;
        this.productRepository = productRepository;
    }

    public int recommendAdditionalPurchase(Product product, Integer purchaseQuantity) {
        ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
        return productInventory.recommendAdditionalPurchase(purchaseQuantity);
    }

    public int getPromotionNonApplicablePurchaseQuantity(Product product, Integer purchaseQuantity) {
        ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
        return productInventory.getPromotionNonApplicablePurchaseQuantity(purchaseQuantity);
    }

    public Order createOrder(List<OrderRequest> orderRequests) {
        Map<Product, Integer> orders = orderRequests.stream()
                .collect(Collectors.toMap(
                        orderRequest -> productRepository.findProduct(orderRequest.productName()),
                        OrderRequest::purchaseQuantity
                ));
        return new Order(orders);
    }
}
