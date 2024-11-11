package store.domain.inventory;

import java.util.List;
import java.util.Objects;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.ProductRepository;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionRepository;
import store.interfaces.ProductRequest;

public class ProductInventoryService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final ProductInventoryRepository productInventoryRepository;

    public ProductInventoryService(ProductRepository productRepository, PromotionRepository promotionRepository,
                                   ProductInventoryRepository productInventoryRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
        this.productInventoryRepository = productInventoryRepository;
    }

    public List<ProductInventory> createProductInventory(List<ProductRequest> productRequests) {
        createProductInventory();
        for (ProductRequest productRequest : productRequests) {
            ProductInventory productInventory = productInventoryRepository.findProductInventory(
                    productRequest.productName());
            if (Objects.isNull(productRequest.promotionName())) {
                productInventory.updateStockQuantity(productRequest.stockQuantity());
                continue;
            }
            Promotion promotion = promotionRepository.findPromotion(productRequest.promotionName());
            productInventory.addPromotion(promotion);
            productInventory.updatePromotionStockQuantity(productRequest.stockQuantity());
        }
        return productInventoryRepository.findAllProductInventory();
    }

    public ProductInventory findProductInventory(String productName) {
        return productInventoryRepository.findProductInventory(productName);
    }

    public void validateStoreInventory(Order order) {
        for(Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = productInventoryRepository.findProductInventory(product.getName());
            productInventory.validateStockAvailable(order.getOrder().get(product));
        }
    }

    private void createProductInventory() {
        productRepository.findAllProducts().stream()
                .map(ProductInventory::new)
                .forEach(productInventoryRepository::saveProductInventory);
    }
}
