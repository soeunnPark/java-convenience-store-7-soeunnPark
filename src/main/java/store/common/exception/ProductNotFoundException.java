package store.common.exception;

import store.domain.product.Product;

public class ProductNotFoundException extends ConvenienceStoreException {

    public ProductNotFoundException(String productName) {
        super(ErrorMessage.PRODUCT_NOT_FOUND, "(상품 이름: " + productName + ")");
    }

    public ProductNotFoundException(String productName, Exception cause) {
        super(ErrorMessage.PRODUCT_NOT_FOUND, "(상품 이름: " + productName + ")", cause);
    }

    public ProductNotFoundException(Product product) {
        super(ErrorMessage.PRODUCT_NOT_FOUND, "(상품 이름: " + product.getName() + " 상품 가격: " + product.getPrice() + ")");
    }

    public ProductNotFoundException(Product product, Exception cause) {
        super(ErrorMessage.PRODUCT_NOT_FOUND, "(상품 이름: " + product.getName() + " 상품 가격: " + product.getPrice() + ")",
                cause);
    }
}
