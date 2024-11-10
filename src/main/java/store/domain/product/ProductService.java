package store.domain.product;

public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product findProduct(String productName) {
        return productRepository.findByProductName(productName);
    }
}
