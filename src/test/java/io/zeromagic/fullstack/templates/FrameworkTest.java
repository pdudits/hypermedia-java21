package io.zeromagic.fullstack.templates;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.lang.StringTemplate.RAW;


import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FrameworkTest {
    @Test
    public void smokeTest() throws IOException {
        var writer = new StringWriter();
        
        record Item(String name) implements Framework.Templated {
            public StringTemplate template() {
                return RAW."<li>\{name}</li>";
            }
        }

        record AList(Collection<Item> items) implements Framework.Templated {
            public StringTemplate template() {
                return RAW."<ul>\{items}</ul>";
            }
        }

        Framework.render(new AList(List.of(new Item("first"), new Item("second"))), writer);
        
        System.out.println(writer.toString());
        assertTrue(writer.toString().startsWith("<ul><li>"));
    }
}
