package io.zeromagic.fullstack.server;

public record HttpMethod(String method, String parameter) implements HttpMethodMatch {}