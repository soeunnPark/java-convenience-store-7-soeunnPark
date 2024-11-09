# java-convenience-store-precourse

## 기능 목록

### 상품 재고
- [x] 상품 수량이 남아있는지 확인한다.

### 주문

- [x] 주문을 수정할 수 있습니다. 
   - [x] 프로모션 할인이 적용되지 않는 일부 수량을 주문에서 제거합니다.
   - [x] 프로모션 적용을 위한 추가 구매 수량을 주문에 추가합니다.

### 할인
- [x] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 추가로 받을 수 있는 상품 개수를 구한다.
- [x] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 혜택 적용이 안되는 일부 수량을 구한다.
- [x] 프로모션 혜택을 받을 증정품의 개수를 구한다.

### 영수증
- [x] 총 구매액을 계산한다.
- [x] 행사할인 금액을 계산한다.
- [x] 멤버십 할인 금액을 계산한다.

## 도메인 객체

### 구매 `Purchase`

- 상태
    - Map<Product, quantity>

### 멤버십 할인 `Membership`

- 프로모션 미적용 금액의 30% 할인
- 한도 8,000원
- 상태
    - 적용 여부
    - 할인 금액

### 프로모션 타입 `PromotionType`

- `MD_RECOMMEND(`”MD추천상품”)
- `SPECIAL_DISCOUNT`(”반짝할인”)
- `BUY_N__GET_ONE_FREE`(”N+1증정”)
- `NONE`(”없음”)


### 프로모션 할인 `Promotion`

- 상태
    - `Product`
    - `PromotionType`
    - 할인 금액
    - 구매 수량과 증정 개수
        - `buy`
        - `get`
    - 프로모션 기간
        - `start_date`
        - `end_date`


### 전체 상품 재고

- 상태
    - `List<ProductInventory>`

### 상품 재고 `ProductInventory`

- 상태
    - 상품 `Product`
    - 수량 `quantity`
    - 프로모션 타입 `PromotionType`

### 상품 `Product`

- 상태
    - 이름 `name`
    - 가격 `price`