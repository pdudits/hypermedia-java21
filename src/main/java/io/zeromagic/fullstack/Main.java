package io.zeromagic.fullstack;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.templates.ShoppingCartHandler;

public class Main {
    public static void main(String... args) throws IOException {
        var cart = new ShoppingCart();
        cart.add("Smartphone", 50000, 1);
        cart.add("Laptop", 100000, 2);
        cart.add("Headphones", 5000, 3);
        cart.add("Smartwatch", 20000, 1);
        cart.add("Camera", 80000, 1);
        // _ doesn't yet work reliably as lambda parameter
        var cartHandler = new ShoppingCartHandler(Main::notFound, _x -> cart);

        var server = HttpServer.create(new InetSocketAddress(8080), 200);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        var context = server.createContext("/", cartHandler);
        server.start();
        

        System.out.println(STR."Server started at port \{server.getAddress()}");
    }

    static void notFound(HttpExchange exchange) throws IOException {
        try (var _x = exchange) {
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(404, 0);
            try (var writer = new  OutputStreamWriter(exchange.getResponseBody(), StandardCharsets.UTF_8)) {
                writer.append(STR."""
                        <!DOCTYPE html>
                        <html>
                        <head>
                        <title>Not Found</title>
                        </head>
                        <body>
                        <h1>Not Found</h1>
                        <p>No handler matched this request</p>
                        </body>
                        </html>
                        """);
            }
        }
    }
}
