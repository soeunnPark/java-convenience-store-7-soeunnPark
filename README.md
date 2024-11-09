# java-convenience-store-precourse


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