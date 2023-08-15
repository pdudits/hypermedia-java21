package io.zeromagic.fullstack.templates;

import static java.lang.StringTemplate.RAW;

public class Index extends Page {
    @Override
    protected String title() {
        return "Fullstack Java with String templates";
    }
    @Override
    public StringTemplate main() {
        return RAW."""
            <div class="grid">
                \{card("Plain HTML", "/plain/", "Cart page using plain HTML, form submission and full redraws")}
                \{card("Unpoly", "/unpoly/", "Cart page using Unpoly, form submission and partial redraws")}
                \{card("HTMX", "/htmx/", "Cart page using HTMX and partial redraws")}
            </div>
            """;
    }

    StringTemplate card(String title, String link, String description) {
        return RAW."""
                <article>
                  <header><h3>\{title}</h3></header>
                  <p>\{description}</p>
                  <footer><a href="\{link}" role="button">Go to page</a></footer>
                </article>
                """;
    }
}