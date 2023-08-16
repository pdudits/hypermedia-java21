package io.zeromagic.fullstack.templates;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.domain.ShoppingCart.Item;

import static java.lang.StringTemplate.RAW;

public class ShoppingCartPage extends Page {
    private final List<Item> items;

    public ShoppingCartPage(List<Item> items) {
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
            <th>Article Name</th> <th>Quantity</th>
            <th>Unit Price</th> <th>Total Price</th>
          </tr>
        </thead>
        <tbody>
          \{items()}
        </tbody>
        <tfoot>
          <tr>
            <td colspan="3"><strong>Total:</strong></td>
            \{total(items)}
            <td></td>
           </tr>
        </tfoot>
        </table>
        </article>
        """;
    }
    
    protected Stream<StringTemplate> items() {
      return items.stream().map(this::item);
    }

    protected StringTemplate item(Item i) {
      return RAW."""
          <tr>
          <td>\{i.article().name()}</td>
          <td>
            <form method="post">
              <button role="button" formaction="decrement/\{i.id()}" }>-</button>
              \{i.quantity()} 
              <button role="button" formaction="increment/\{i.id()}">+</button>
            </form>
          </td>
          <td>\{i.article().decimalPrice()}</td>
          <td>\{i.totalPrice()}</td>
          </tr>
          """;
    }

    protected StringTemplate total(Collection<Item> items) {
      return RAW."""
          <td id="total">\{itemTotal(items)}</td>
          """;
    }

    protected static BigDecimal itemTotal(Collection<Item> items) {
      return items.stream().map(i -> i.totalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
