package store.domain.product;

import java.util.List;
import store.interfaces.product.ProductRequest;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProducts(List<ProductRequest> productRequests) {
        productRequests.stream()
                .filter(productRequest -> !productRepository.existByProductName(productRequest.productName()))
                .map(productRequest -> new Product(productRequest.productName(), productRequest.productPrice()))
                .forEach(productRepository::save);
    }
}
