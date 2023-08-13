package io.zeromagic.fullstack.templates;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StringTemplate.Processor;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;

import io.zeromagic.fullstack.server.Response;

public interface Framework {

    static Response ok(StringTemplate template) {
        return Response.ok("text/html", out -> template.process(new WriterProcessor(out)));
    }

    static Response ok(StringTemplate... templates) {
        return Response.ok("text/html", out -> new WriterProcessor(out).processValue(Stream.of(templates)));
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
                case Iterable<?> c -> processIterator(c.iterator());
                case Stream<?> c -> processIterator(c.iterator());
                default -> out.write(String.valueOf(value));
            }
        }

        private void processIterator(Iterator<?> it) throws IOException {
            while (it.hasNext()) {
                var e = it.next();
                processValue(e);
            }
        }
    }
}
