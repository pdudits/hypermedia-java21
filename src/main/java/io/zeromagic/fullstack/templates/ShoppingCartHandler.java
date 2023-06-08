package io.zeromagic.fullstack.templates;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Request;

import io.zeromagic.fullstack.domain.ShoppingCart;

public class ShoppingCartHandler implements HttpHandler {

    private HttpHandler fallbackHandler;
    private Function<Request, ShoppingCart> cartLoader;

    public ShoppingCartHandler(HttpHandler fallbackHandler, Function<Request, ShoppingCart> cartLoader ) {
        this.fallbackHandler = fallbackHandler;
        this.cartLoader = cartLoader;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            exchange.getRe
            switch (exchange.getRequestMethod()) {
                case "GET" -> renderCart(exchange);
                default -> fallbackHandler.handle(exchange);
            }
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, e, () -> "Failed to process request");
        } finally {
            exchange.close();
        }
    }
    
    void renderCart(HttpExchange exchange) throws IOException {
        var cart = cartLoader.apply(exchange);
        if (cart == null) {
            fallbackHandler.handle(exchange);
            return;
        }
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);
        try (var writer = new OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8)) {
            Framework.render(new ShoppingCartPage(cart.items()), writer);
        }
    }
}
