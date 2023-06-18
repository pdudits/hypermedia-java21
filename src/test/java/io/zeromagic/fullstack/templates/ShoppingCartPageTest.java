package io.zeromagic.fullstack.templates;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.zeromagic.fullstack.domain.ShoppingCart.Article;
import io.zeromagic.fullstack.domain.ShoppingCart.Item;

public class ShoppingCartPageTest {
    static List<Item> items = List.of(
        new Item(0, new Article("Smartphone", 500_00), 1),
        new Item(1, new Article("Laptop", 1000_00), 1),
        new Item(2, new Article("Headphones", 100_00), 1),
        new Item(3, new Article("Smartwatch", 200_00), 1),
        new Item(4, new Article("Camera", 800_00), 1));

    @Test
    public void testRender() {
        var result = Framework.render(new ShoppingCartPage(items));
        System.out.println(result);
    }
}
