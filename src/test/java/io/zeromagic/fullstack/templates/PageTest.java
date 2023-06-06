package io.zeromagic.fullstack.templates;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PageTest {
    @Test
    public void simplePageTest() {
        var page = new Page() {

            @Override
            protected String title() {
                return "Hello";
            }

            @Override
            protected Object main() {
                return null;
            }};
        var out = Framework.render(page);
        System.out.println(out);
        assertTrue(out.contains("<title>Hello"));
    }
}
