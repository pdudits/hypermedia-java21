package io.zeromagic.fullstack.templates.unpoly;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.domain.ShoppingCart.Item;
import io.zeromagic.fullstack.templates.ShoppingCartPage;

import static java.lang.StringTemplate.RAW;

public class UnpolyShoppingCartPage extends ShoppingCartPage {

    public UnpolyShoppingCartPage(List<Item> items) {
      super(items);
    }

    @Override
    protected String libraryInit() {
      return """
      <script src="https://unpkg.com/unpoly@3.3.0/unpoly.min.js"></script>
      <link rel="stylesheet" href="https://unpkg.com/unpoly@3.3.0/unpoly.min.css">
      """;
    }

    @Override
    protected StringTemplate item(Item i) {
      return RAW."""
          <tr>
          <td>\{i.article().name()}</td>
          <td>
            <form method="post" up-submit up-target="table">
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

}
