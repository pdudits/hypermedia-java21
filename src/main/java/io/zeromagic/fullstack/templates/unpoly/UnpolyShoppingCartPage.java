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
              <script>
                     up.link.config.followSelectors.push('a[href]');
                     up.link.config.noFollowSelectors.push('nav a[href]');
              </script>
              <link rel="stylesheet" href="https://unpkg.com/unpoly@3.3.0/unpoly.min.css">
              <style type="text/css">
                up-modal-box {
                   background: none;
                   box-shadow: none;
                }
              </style>      
              """;
    }

    @Override
    protected StringTemplate item(Item i) {
      return RAW."""
          <tr>
          <td>\{i.article().name()}</td>
          <td>
            <form method="post" up-submit up-target="table">
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

}
