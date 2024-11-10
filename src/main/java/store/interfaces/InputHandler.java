package store.interfaces;

import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import store.common.exception.InvalidConfirmResponseException;
import store.common.exception.InvalidFileException;
import store.common.exception.InvalidOrderFormException;
import store.interfaces.AdditionalPurchase.Request;

public class InputHandler {

    public List<ProductRequest> readProducts() {
        String fileName = "products.md";
        List<ProductRequest> productsRequest = new ArrayList<>();
        try (BufferedReader bufferedReader = readFile(fileName)) {
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] splitInput = input.split(",");
                productsRequest.add(ProductRequest.of(splitInput[0], splitInput[1], splitInput[2], splitInput[3]));
            }
        } catch (IOException e) {
            throw new InvalidFileException(fileName, e);
        }
        return productsRequest;
    }

    public List<PromotionRequest> readPromotions()  {
        String fileName = "promotions.md";
        List<PromotionRequest> promotionsRequest = new ArrayList<>();
        try (BufferedReader bufferedReader = readFile(fileName)) {
            String input;
            while ((input = bufferedReader.readLine()) != null) {
                String[] splitInput = input.split(",");
                promotionsRequest.add(PromotionRequest.of(splitInput[0], splitInput[1], splitInput[2], splitInput[3], splitInput[4]));
            }
        } catch (IOException e) {
            throw new InvalidFileException(fileName);
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

    public AdditionalPurchase.Request askAdditionalPurchaseForPromotion(
            AdditionalPurchase.Response additionalPurchaseResponse) {
        System.out.println("현재 " + additionalPurchaseResponse.productName() + "은(는) "
                + additionalPurchaseResponse.recommendedAdditionalPurchaseQuantity()
                + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        AdditionalPurchase.Request confirmAdditionalPurchase = new Request(input);
        confirmAdditionalPurchase.valid();
        return confirmAdditionalPurchase;
    }

    public PromotionNonApplicablePurchase.Request askExcludeNonPromotion(
            PromotionNonApplicablePurchase.Response promotionNonApplicablePurchaseResponse) {
        System.out.println("현재 " + promotionNonApplicablePurchaseResponse.productName() + " "
                + promotionNonApplicablePurchaseResponse.promotionNonApplicablePurchaseQuantity()
                + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        PromotionNonApplicablePurchase.Request confirmExcludeNonPromotion = new PromotionNonApplicablePurchase.Request(
                input);
        confirmExcludeNonPromotion.valid();
        return confirmExcludeNonPromotion;
    }

    public String readMembership() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        String input = readLineWithoutSpace();
        if(!input.equals("Y") && !input.equals("N")) {
            throw new InvalidConfirmResponseException(input);
        }
        return input;
    }

    private String readLineWithoutSpace() {
        return Console.readLine().trim();
    }

    private BufferedReader readFile(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(
                    Objects.requireNonNull(getClass().getClassLoader().getResource(fileName)).getFile());
            return new BufferedReader(new InputStreamReader(fileInputStream));
        } catch (IOException | NullPointerException e) {
            throw new InvalidFileException(fileName, e);
        }
    }


}
