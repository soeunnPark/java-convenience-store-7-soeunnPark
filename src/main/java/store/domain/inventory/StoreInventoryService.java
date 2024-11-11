package store.domain.inventory;

import java.util.List;
import java.util.Objects;
import store.common.exception.PromotionNotExistException;
import store.domain.order.Order;
import store.domain.product.Product;
import store.domain.product.ProductRepository;
import store.domain.promotion.Promotion;
import store.domain.promotion.PromotionRepository;
import store.interfaces.ProductRequest;
import store.interfaces.PromotionRequest;

public class StoreInventoryService {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;

    public StoreInventoryService(ProductRepository productRepository, PromotionRepository promotionRepository) {
        this.productRepository = productRepository;
        this.promotionRepository = promotionRepository;
    }

    public StoreInventory makeStoreInventory(List<ProductRequest> productRequests, List<PromotionRequest> promotionRequests) {

        StoreInventory storeInventory = new StoreInventory();

        for (ProductRequest productRequest : productRequests) {
            Product product = productRepository.findByProductName(productRequest.productName());
            Promotion promotion = null;
            if (Objects.nonNull(productRequest.promotionName())) {
                promotion = promotionRepository.findPromotion(productRequest.promotionName())
                        .orElseThrow(() -> new PromotionNotExistException(productRequest.promotionName()));
            }
            if (!storeInventory.existsByProduct(product)) {
                ProductInventory productInventory = new ProductInventory(product, promotion);
                storeInventory.addProductInventory(productInventory);
            }

            ProductInventory productInventory = storeInventory.getProductInventory(product);

            if (Objects.isNull(productRequest.promotionName())) {
                productInventory.updateStockQuantity(productRequest.stockQuantity());
            } else {
                productInventory.updatePromotionStockQuantity(productRequest.stockQuantity());
            }
        }
        return storeInventory;
    }

    public void validateStoreInventory(Order order, StoreInventory storeInventory) {
        for(Product product : order.getOrder().keySet()) {
            ProductInventory productInventory = storeInventory.getProductInventory(product);
            productInventory.validateStockAvailable(order.getOrder().get(product));
        }
    }
}
