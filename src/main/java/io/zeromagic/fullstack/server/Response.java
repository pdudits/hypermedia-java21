package io.zeromagic.fullstack.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public record Response(int statusCode, Map<String, String> headers, BodyOutput body) {

    public static Response ok(String contentType, BodyWriter body) {
        return new Response(200, Map.of("Content-Type", contentType + ";charset=UTF-8"), utf8(body));
    }

    public static Response html(int status, BodyWriter body) {
        return new Response(status, Map.of("Content-Type", "text/html;charset=UTF-8"), utf8(body));
    }

    public static BodyOutput encoded(Charset charset, BodyWriter body) {
        return out -> {
            try (var writer = new OutputStreamWriter(out, charset)) {
                body.writeTo(writer);
            }
        };
    }

    public static BodyOutput utf8(BodyWriter body) {
        return encoded(StandardCharsets.UTF_8, body);
    }

    public static Response notFound() {
        return new Response(404, Map.of(), utf8(out -> out.append("Not Found")));
    }

    public interface BodyWriter {
        void writeTo(Writer out) throws IOException;
    }

    public interface BodyOutput {
        void writeTo(OutputStream out) throws IOException;
    }
}
