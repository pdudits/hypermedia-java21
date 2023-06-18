package io.zeromagic.fullstack.templates;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.zeromagic.fullstack.server.*;

import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.server.Response;

public class ShoppingCartHandler implements Server.Handler {

    private Function<Request, ShoppingCart> cartLoader;

    public ShoppingCartHandler(Function<Request, ShoppingCart> cartLoader ) {
        this.cartLoader = cartLoader;
    }

    @Override
    public Response handle(Request request) throws IOException {
        var cart = cartLoader.apply(request);
        if (cart == null) {
            return null;
        }
        return switch (request.matchMethodPath()) {
            case GET(var p) when p == null -> renderCart(cart);
            case POST(var action) -> switch(action) {
                case "increment" -> incrementItem(cart, Integer.parseInt(request.getPathSegment(1)));
                case "decrement" -> decrementItem(cart, Integer.parseInt(request.getPathSegment(1)));
                default -> null;
            };
            default -> null;
        };
    }

    private Response incrementItem(ShoppingCart cart, int itemId) throws IOException {
        var item = cart.increment(itemId);
        return Framework.ok(ShoppingCartPage.item(item));
    }

    private Response decrementItem(ShoppingCart cart, int itemId) throws IOException {
        var item = cart.decrement(itemId);
        return Framework.ok(ShoppingCartPage.item(item));
    }

    private Response renderCart(ShoppingCart cart) throws IOException {
        return Framework.ok(new ShoppingCartPage(cart.items()));
    }
    
}
