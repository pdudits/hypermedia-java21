package io.zeromagic.fullstack.server;

import java.io.IOException;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class Server {
    static class ErrorHandler extends Filter {

        @Override
        public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
            exchange.
        }

        @Override
        public String description() {
            return "Handle runtime errors and proper close of exchange";
        }
        
    }
}
