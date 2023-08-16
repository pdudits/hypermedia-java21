package io.zeromagic.fullstack.templates.plain;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.zeromagic.fullstack.server.*;

import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.server.Response;
import io.zeromagic.fullstack.templates.Framework;
import io.zeromagic.fullstack.templates.ShoppingCartPage;

public class PlainHandler implements Server.Handler {

    private Function<Request, ShoppingCart> cartLoader;

    public PlainHandler(Function<Request, ShoppingCart> cartLoader ) {
        this.cartLoader = cartLoader;
    }

    @Override
    public Response handle(Request request) throws IOException {
        var cart = cartLoader.apply(request);
        if (cart == null) {
            return null;
        }
        var methodAndPath = request.matchMethodPath();
        System.out.println(methodAndPath);
        return switch (methodAndPath) {
            // not as good as hoped for, I cannot write GET(null) or POST("increment") directly
            // that gets interpreted as method call instead of pattern matching (which makes sense on second thought)
            case GET(var p) when "".equals(p) -> renderCart(cart);
            case POST(var action) -> switch(action) {
                case "increment" -> incrementItem(cart, request, Integer.parseInt(request.getPathSegment(1)));
                case "decrement" -> decrementItem(cart, request, Integer.parseInt(request.getPathSegment(1)));
                // null has to be explicitly added, even in presence of default
                case null, default -> null;
            };
            default -> null;
        };
    }

    private Response incrementItem(ShoppingCart cart, Request request, int itemId) throws IOException {
        var item = cart.increment(itemId);
        return Response.found(request.resolve("/plain/"));
    }

    private Response decrementItem(ShoppingCart cart, Request request, int itemId) throws IOException {
        var item = cart.decrement(itemId);
        return Response.found(request.resolve("/plain/"));
    }

    private Response renderCart(ShoppingCart cart) throws IOException {
        return Framework.ok(new ShoppingCartPage(cart.items()));
    }
    
}
