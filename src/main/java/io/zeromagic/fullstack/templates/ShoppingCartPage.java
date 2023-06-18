package io.zeromagic.fullstack.templates;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import io.zeromagic.fullstack.domain.ShoppingCart.Item;

import static java.lang.StringTemplate.RAW;

public class ShoppingCartPage extends Page {
    private final List<Item> items;

    ShoppingCartPage(List<Item> items) {
      this.items = items;
    }

    @Override
    protected String title() {
        return "Shopping cart";
    }

    @Override
    protected String extraStyles() {
        return """
        td > input[type], td > button {
            margin-bottom: 0;
        }                  

        table {
            margin-left: calc(-1 * var(--spacing));
            margin-right: calc(-1 * var(--spacing));
            width: auto
        }
        """;
    }

    @Override
    protected StringTemplate main() {
        return RAW."""
        <article>
        <h1>\{title()}</h1>
        <table>
        <thead>
          <tr>
            <th>Article Name</th>
            <th>Quantity</th>
            <th>Unit Price</th>
            <th>Total Price</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
           \{items()}
        </tbody>
        <tfoot>
          <tr>
            <td colspan="3"><strong>Total:</strong></td>
            <td>\{itemTotal()}</td>
            <td></td>
           </tr>
        </tfoot>
        </table>
        </article>
        """;
    }
    
    private Stream<StringTemplate> items() {
      return items.stream().map(ShoppingCartPage::item);
    }

    static StringTemplate item(Item i) {
      return RAW."""
          <tr>
          <td>\{i.article().name()}</td>
          <td>\{i.quantity()}</td>
          <td>\{i.article().decimalPrice()}</td>
          <td>\{i.article().decimalPrice().multiply(BigDecimal.valueOf(i.quantity()))}</td>
          </tr>
          """;
    }

    private BigDecimal itemTotal() {
      return items.stream().map(i -> i.article().decimalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
