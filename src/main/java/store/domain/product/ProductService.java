package store.domain.product;

import java.util.List;
import store.interfaces.ProductRequest;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProducts(List<ProductRequest> productRequests) {
        List<Product> products = productRequests.stream()
                .filter(productRequest -> !productRepository.existByProductName(productRequest.productName()))
                .map(productRequest -> new Product(productRequest.productName(), productRequest.productPrice()))
                .toList();
        products.forEach(productRepository::save);
    }

    public Product findProduct(String productName) {
        return productRepository.findByProductName(productName);
    }
}
