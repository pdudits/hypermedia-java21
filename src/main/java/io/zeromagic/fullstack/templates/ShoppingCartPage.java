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
                table {
                    margin-left: calc(-1 * var(--spacing));
                    margin-right: calc(-1 * var(--spacing));
                }
                
                table * {
                    --typography-spacing-vertical: 0;
                }
                
                table button {
                    --spacing: 0;
                }
    
                td.price {
                  text-align: right;
                }                      

                tbody tr td:nth-child(2), tbody tr td:nth-child(2) > * {
                  display: flex;
                  align-items: baseline;
                  flex-wrap: nowrap;
                  & * {
                    flex: 1 1 auto;
                  }
                }
                       
                td strong {
                       min-width: 2em;
                       text-align: center;
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
              \{button("decrement", i.id(), "-")}
              <strong>\{i.quantity()}</strong>
              \{button("increment", i.id(), "+")}
            </form>
          </td>
          <td class="price">\{i.article().decimalPrice()}</td>
          <td class="price">\{i.totalPrice()}</td>
          </tr>
          """;
    }
    
    protected StringTemplate button(String action, int id, String label) {
        return RAW."""
                   <button role="button" formaction="\{action}/\{id}">\{label}</button>""";
    }

    protected StringTemplate total(Collection<Item> items) {
      return RAW."""
          <td id="total" class="price">\{itemTotal(items)}</td>
          """;
    }

    protected static BigDecimal itemTotal(Collection<Item> items) {
      return items.stream().map(i -> i.totalPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
