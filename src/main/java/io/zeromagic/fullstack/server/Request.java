package io.zeromagic.fullstack.server;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class Request {
    private final HttpExchange exchange;
	private final String contextPath;
    private Map<String,String> queryParameters;

	Request(HttpExchange exchange, String contextPath) {
        this.exchange = exchange;
        this.contextPath = contextPath;
    }

    /**
     * Return segment of request path of this request after handler's context path
     * @param index
     * @return
     */
    public String getPathSegment(int index) {
        String requestPath = exchange.getRequestURI().getPath();
        String[] segments = requestPath.substring(contextPath.length()).split("/");
        if (segments.length > index + 1) {
            return segments[index + 1];
        }
        return null;
    }

    /**
     * Return value of query parameter or null if not defined. If there are multiple parameters
     * of same name it will return first one present in request uri
     * @param parameterName
     * @return
     */
    public String getQueryParameter(String parameterName) {
        if (queryParameters == null) {
            queryParameters = parseQueryParameters();
        }
        return queryParameters.get(parameterName);
    }

    private Map<String, String> parseQueryParameters() {
        var queryParams = new HashMap<String, String>();
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            String[] queryParamsArray = query.split("&");
            for (String param : queryParamsArray) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    var key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                    var value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                    queryParams.put(key, value);
                }
            }
        }
        return queryParams;
    }   

    /**
     * Return mime type of request body
     * @return
     */
    public String getBodyType() {
        return exchange.getRequestHeaders().getFirst("Content-Type");
    }

    /**
     * Returns parsed url-formencoded body if request is of appropriate type, null otherwise.
     * If there are multiple parameters of same name, the first one is returned.
     */
    public Map<String, String> getFormParameters() throws IOException {
        if ("application/x-www-form-urlencoded".equals(getBodyType())) {
            byte[] requestBody = exchange.getRequestBody().readAllBytes();
            String body = new String(requestBody, StandardCharsets.UTF_8);
            Map<String, String> formParameters = new HashMap<>();
            String[] params = body.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=", 2);  // Limit split to 2 parts
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8.name());
                formParameters.put(key, value);
            }
            return formParameters;
        }
        return null;
    }
}
