package store.interfaces;

import store.common.exception.InvalidConfirmResponseException;
import store.domain.product.Product;

public class PromotionNonApplicablePurchase {
    public record Response(
            String productName,
            int promotionNonApplicablePurchaseQuantity
    ) {
        public static PromotionNonApplicablePurchase.Response of(Product product, int promotionNonApplicablePurchaseQuantity) {
            return new PromotionNonApplicablePurchase.Response(product.getName(), promotionNonApplicablePurchaseQuantity);
        }
    }

    public record Request(
            String excludeNonPromotion
    ) {
        public void valid() {
            if(!this.excludeNonPromotion.equals("Y") && !this.excludeNonPromotion.equals("N")) {
                throw new InvalidConfirmResponseException(this.excludeNonPromotion);
            }
        }
    }
}
