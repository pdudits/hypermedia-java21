package io.zeromagic.fullstack.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ShoppingCart {
    public record Article(String name, int price) {
        public BigDecimal decimalPrice() {
            return BigDecimal.valueOf(price, 2);
        }
    }
    
    public record Item(int id, Article article, int quantity) {
        public Item increment() {
            return new Item(id, article, quantity+1);
        }

        public Item decrement() {
            return quantity > 1 ? new Item(id, article, quantity-1) : this;
        }

        public BigDecimal totalPrice() {
            return article.decimalPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }

    private ArrayList<Item> items = new ArrayList<>();

    public List<Item> items() {
        return this.items;
    }

    public Item increment(int index) {
        return update(index, Item::increment);
    }

    public Item decrement(int index) {
        return update(index, Item::decrement);
    }

    public void remove(int index) {
        items.remove(index);
    }

    public ShoppingCart add(String name, int priceInCents, int quantity) {
        items.add(new Item(items.size(), new Article(name, priceInCents), quantity));
        return this;
    }

    private Item update(int index, Function<Item,Item> transform) {
        var newItem = transform.apply(items.get(index));
        items.set(index, newItem);
        return newItem;
    }
}
