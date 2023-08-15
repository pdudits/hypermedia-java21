package io.zeromagic.fullstack.templates;

import io.zeromagic.fullstack.server.*;

public class IndexHandler implements Server.Handler {
    @Override
    public Response handle(Request request) {
        if (request.matchMethodPath() instanceof GET) {
            return Framework.ok(new Index());
        }
        return null;
    }
     
}