package io.zeromagic.fullstack;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import io.zeromagic.fullstack.templates.IndexHandler;
import io.zeromagic.fullstack.domain.ShoppingCart;
import io.zeromagic.fullstack.templates.htmx.HtmxShoppingCartHandler;
import io.zeromagic.fullstack.templates.plain.PlainHandler;
import io.zeromagic.fullstack.templates.unpoly.UnpolyHandler;
import io.zeromagic.fullstack.server.Server;

public class Main {
    public static void main(String... args) throws IOException {
        var cart = new ShoppingCart();
        cart.add("Smartphone", 50000, 1);
        cart.add("Laptop", 100000, 2);
        cart.add("Headphones", 5000, 3);
        cart.add("Smartwatch", 20000, 1);
        cart.add("Camera", 80000, 1);

        var server = new Server(8080);
        // JEP 443 (preview) using _ causes LinkageError: https://bugs.openjdk.org/browse/JDK-8313323
        // Error: LinkageError occurred while loading main class io.zeromagic.fullstack.Main
        // java.lang.ClassFormatError: Illegal field name "" in class io/zeromagic/fullstack/Main
        var cartHandler = new HtmxShoppingCartHandler(_c -> cart);
        server.addHandler("/", new IndexHandler());
        server.addHandler("/htmx/", cartHandler);
        server.addHandler("/plain/", new PlainHandler(_c -> cart));
        server.addHandler("/unpoly/", new UnpolyHandler(_c -> cart));
        server.start();       

        System.out.println(STR."Server started at port \{server.getAddress()}");
    }

}
