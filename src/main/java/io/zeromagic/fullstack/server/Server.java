package io.zeromagic.fullstack.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Server {
    private final com.sun.net.httpserver.HttpServer server;
    private static final System.Logger LOGGER = System.getLogger("Server");

    public Server(HttpServer server) {
        this.server = server;
    }

    public Server(int port) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(8080), 200);
        this.server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }

    public void start() {
        server.start();
    }

    public void stop(int delay) {
        server.stop(delay);
    }

    /**
     * Add a handler for a sub-path
     */
    public void addHandler(String contextPath, Handler handler) {
        server.createContext(contextPath, new HandlerAdapter(handler, contextPath));
    }

    public InetSocketAddress getAddress() {
        return server.getAddress();
    }

    /**
     * Request processor.
     */
    public interface Handler {
        /**
         * Handle the request, returning non-null response if it matches any of the
         * paths
         * @param request request to process
         * @return a response, if the request matched any of the actions of this handler
         * @throws IOException
         */
        Response handle(Request request) throws IOException;
    }

    void handleException(HttpExchange exchange, Exception e) throws IOException {
        LOGGER.log(System.Logger.Level.ERROR, "Rendering error", e);
        exchange.getResponseHeaders().set("Content-Type", "text/plain;charset=UTF-8");
        exchange.sendResponseHeaders(500, 0);
        try (var response = new PrintWriter(exchange.getResponseBody(), true, StandardCharsets.UTF_8)) {
            e.printStackTrace(response);
        };
    }

    Response notFound() {
        return Response.html(404, out -> out.append(STR."""
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
                        """));
    }


    class HandlerAdapter implements HttpHandler {
        private final Handler handler;
        private final String contextPath;

        HandlerAdapter(Handler handler, String contextPath) {
            this.handler = handler;
            this.contextPath = contextPath;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var request = new Request(exchange, contextPath);
            try {
                var response = handler.handle(request);
                if (response == null) {
                    response = notFound();
                }
                response.headers().forEach((name, value) -> exchange.getResponseHeaders().add(name, value));
                exchange.sendResponseHeaders(response.statusCode(), 0);
                response.body().writeTo(exchange.getResponseBody());
            } catch (Exception e) {
                handleException(exchange, e);
            } finally {
                exchange.close();
            }
        }
    }
}
