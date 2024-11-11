package store.interfaces;

import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.common.exception.InvalidConfirmResponseException;
import store.common.exception.InvalidFileException;
import store.common.exception.InvalidOrderFormException;

public class InputHandler {

    public List<ProductRequest> readProducts() {
        List<ProductRequest> productsRequest = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/products.md"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split(",");
                productsRequest.add(ProductRequest.of(splitInput[0], splitInput[1], splitInput[2], splitInput[3]));
            }
            br.close();
        } catch (IOException e) {
            throw new InvalidFileException("products.md");
        }
        return productsRequest;
    }

    public List<PromotionRequest> readPromotions()  {
        List<PromotionRequest> promotionsRequest = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/promotions.md"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split(",");
                promotionsRequest.add(PromotionRequest.of(splitInput[0], splitInput[1], splitInput[2], splitInput[3], splitInput[4]));
            }
            br.close();
        } catch (IOException e) {
            throw new InvalidFileException("promotions.md");
        }
        return promotionsRequest;
    }

    public List<OrderRequest> readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<OrderRequest> ordersRequest = new ArrayList<>();
        String[] orders = readLineWithoutSpace().split(",");
        for(String s : orders) {
            if(!s.matches("^\\[.+?-\\d+\\]$")) {
                throw new InvalidOrderFormException(s);
            }
            String[] order = s.split("-");
            ordersRequest.add(OrderRequest.of(order[0].substring(1),
            order[1].substring(0, order[1].length()-1)));
        }
        return ordersRequest;
    }

    public boolean askAdditionalPurchaseForPromotion(
            AdditionalPurchaseResponse additionalPurchaseResponse) {
        System.out.println("현재 " + additionalPurchaseResponse.productName() + "은(는) "
                + additionalPurchaseResponse.recommendedAdditionalPurchaseQuantity()
                + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        return convertInputForConfirm(input);
    }

    public boolean askExcludeNonPromotion(PromotionNonApplicablePurchaseResponse promotionNonApplicablePurchaseResponse) {
        System.out.println("현재 " + promotionNonApplicablePurchaseResponse.productName() + " "
                + promotionNonApplicablePurchaseResponse.promotionNonApplicablePurchaseQuantity()
                + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        return convertInputForConfirm(input);
    }

    public boolean readMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        return convertInputForConfirm(input);
    }

    public boolean askContinue() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        String input = readLineWithoutSpace();
        return convertInputForConfirm(input);
    }

    private String readLineWithoutSpace() {
        return Console.readLine().trim();
    }

    private boolean convertInputForConfirm(String input) {
        if(input.equals("Y")) {
            return true;
        } else if(input.equals("N")) {
            return false;
        }
        throw new InvalidConfirmResponseException(input);
    }

    public void closeConsole() {
        Console.close();
    }
}
