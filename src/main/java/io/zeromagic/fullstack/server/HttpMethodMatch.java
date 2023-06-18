package io.zeromagic.fullstack.server;

public sealed interface HttpMethodMatch permits GET, POST, PUT, DELETE, HttpMethod {
    
}
