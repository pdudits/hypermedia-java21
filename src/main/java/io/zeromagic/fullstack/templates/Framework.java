package io.zeromagic.fullstack.templates;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StringTemplate.Processor;
import java.util.Collection;
import java.util.stream.Stream;

import io.zeromagic.fullstack.server.Response;

public interface Framework {

    static Response ok(StringTemplate template) {
        return Response.ok("text/html", out -> template.process(new WriterProcessor(out)));
    }

    static Response ok(Templated root) {
        return ok(root.template());
    }

    interface Templated {
        StringTemplate template();
    }

    static String render(Templated root) {
        var writer = new StringWriter();
        try {
            render(root, writer);
        } catch (IOException e) {
            throw new IllegalStateException("StringWriter throwing IOException", e);
        }
        return writer.toString();
    }

    static void render(Templated root, Writer out) throws IOException {
        root.template().process(new WriterProcessor(out));
    }

    record WriterProcessor(Writer out) implements Processor<Void,IOException> {
        
        @Override
        public Void process(StringTemplate stringTemplate) throws IOException {
            var fragments = stringTemplate.fragments().iterator();
            for (Object value : stringTemplate.values()) {
                out.write(fragments.next());
                processValue(value);
            }
            out.write(fragments.next());
            return null;
        }

        private void processValue(Object value) throws IOException {
            switch (value) {
                case null -> {}
                case Templated t -> t.template().process(this);
                case StringTemplate t -> t.process(this);
                case Iterable<?> c -> {
                    for (var it = c.iterator(); it.hasNext();) {
                        var e = it.next();
                        processValue(e);
                        if (it.hasNext() && !(e instanceof Templated || e instanceof StringTemplate)) {
                            out.write(" ");
                        }
                    }
                }
                case Stream<?> c -> c.forEach(t -> {
                    try {
                        processValue(t);
                    } catch (IOException e) {
                        sneakyThrow(e);
                    }
                });
                default -> out.write(String.valueOf(value));
            }
        }

        @SuppressWarnings("unchecked")
        private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
            throw (E)e;
        } 
    }
}
