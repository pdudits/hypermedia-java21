package io.zeromagic.fullstack.templates;

import java.math.BigDecimal;

interface ShoppingCart {
    record Article(String name, int price) {
        BigDecimal decimalPrice() {
            return BigDecimal.valueOf(price, 2);
        }
    }
    record Item(Article article, int quantity) {}
}
