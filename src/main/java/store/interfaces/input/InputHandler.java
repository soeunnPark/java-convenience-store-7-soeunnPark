package store.interfaces.input;

import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.common.exception.ConvenienceStoreException;
import store.common.exception.InvalidConfirmResponseException;
import store.common.exception.InvalidFileException;
import store.common.exception.InvalidOrderFormException;
import store.interfaces.product.ProductRequest;
import store.interfaces.promotion.PromotionNonApplicablePurchaseResponse;
import store.interfaces.promotion.PromotionRequest;
import store.interfaces.order.AdditionalPurchaseResponse;
import store.interfaces.order.OrderRequest;

public class InputHandler {

    static final String PRODUCTS_PATH = "src/main/resources/products.md";
    static final String PROMOTIONS_PATH = "src/main/resources/promotions.md";
    static final String DELIMITER = ",";

    public List<ProductRequest> readProducts() {
        List<ProductRequest> productsRequest = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileFromResource("products.md")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split(DELIMITER);
                productsRequest.add(ProductRequest.of(splitInput[0].trim(), splitInput[1].trim(), splitInput[2].trim(), splitInput[3].trim()));
            }
            br.close();
        } catch (IOException e) {
            throw new InvalidFileException(PRODUCTS_PATH);
        }
        return productsRequest;
    }

    public List<PromotionRequest> readPromotions() {
        List<PromotionRequest> promotionsRequest = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileFromResource("promotions.md")));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split(DELIMITER);
                promotionsRequest.add(
                        PromotionRequest.of(splitInput[0].trim(), splitInput[1].trim(), splitInput[2].trim(), splitInput[3].trim(), splitInput[4].trim()));
            }
            br.close();
        } catch (IOException e) {
            throw new InvalidFileException(PROMOTIONS_PATH);
        }
        return promotionsRequest;
    }

    public List<OrderRequest> readOrder() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        List<OrderRequest> ordersRequest = new ArrayList<>();
        String[] orders = readLineWithoutSpace().split(",");
        for (String s : orders) {
            if (!s.matches("^\\[.+?-\\d+]$")) {
                throw new InvalidOrderFormException(s);
            }
            String[] order = s.split("-");
            ordersRequest.add(OrderRequest.of(order[0].substring(1),
                    order[1].substring(0, order[1].length() - 1)));
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

    public boolean askExcludeNonPromotion(
            PromotionNonApplicablePurchaseResponse promotionNonApplicablePurchaseResponse) {
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
        while (true) {
            try {
                System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
                String input = readLineWithoutSpace();
                return convertInputForConfirm(input);
            } catch (ConvenienceStoreException e) {
                System.out.println(e.getErrorMessageForClient());
            }
        }
    }

    private String readLineWithoutSpace() {
        return Console.readLine().trim();
    }

    private boolean convertInputForConfirm(String input) {
        if (input.equals("Y")) {
            return true;
        } else if (input.equals("N")) {
            return false;
        }
        throw new InvalidConfirmResponseException(input);
    }

    private File getFileFromResource(String fileName) {
        ClassLoader classLoader = InputHandler.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }

    public void closeConsole() {
        Console.close();
    }
}
