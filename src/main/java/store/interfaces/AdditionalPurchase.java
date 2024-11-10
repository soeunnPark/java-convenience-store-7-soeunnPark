package store.interfaces;

import store.common.exception.InvalidConfirmResponseException;
import store.domain.product.Product;

public class AdditionalPurchase {

    public record Response(
            String productName,
            int recommendedAdditionalPurchaseQuantity
    ) {
        public static AdditionalPurchase.Response of(Product product, int recommendedAdditionalPurchaseQuantity) {
            return new AdditionalPurchase.Response(product.getName(), recommendedAdditionalPurchaseQuantity);
        }
    }

    public record Request(
            String addGiveaway
    ) {
        public void valid() {
            if(!this.addGiveaway.equals("Y") && !this.addGiveaway.equals("N")) {
                throw new InvalidConfirmResponseException(this.addGiveaway);
            }
        }
    }
}
