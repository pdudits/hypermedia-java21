package io.zeromagic.fullstack.templates.htmx;

import java.util.List;
import java.util.Collection;
import io.zeromagic.fullstack.templates.Framework;

import io.zeromagic.fullstack.templates.ShoppingCartPage;
import io.zeromagic.fullstack.domain.ShoppingCart.Item;
import static java.lang.StringTemplate.RAW;

public class HtmxShoppingCartPage extends ShoppingCartPage {
    public HtmxShoppingCartPage(List<Item> items) {
        super(items);
    }

    @Override
    protected String libraryInit() {
        return HtmxLibrary.INSTANCE.initCode();
    }

    @Override
    protected StringTemplate item(Item i) {
        return RAW."""
            <tr hx-target="this" hx-swap="outerHTML">
            <td>\{i.article().name()}</td>
            <td>
                <div>
                <a role="button" href="#" hx-post="decrement/\{i.id()}" }>-</a>
                <strong>\{i.quantity()}</strong>
                <a role="button" href="#" hx-post="increment/\{i.id()}">+</a>
                </div>
            </td>
            <td>\{i.article().decimalPrice()}</td>
            <td>\{i.totalPrice()}</td>
            </tr>
            """;
    }

    @Override
    protected StringTemplate total(Collection<Item> items) {
        return RAW."""
            <td id="total" hx-swap-oob="#total">\{itemTotal(items)}</td>
            """;
    }

}