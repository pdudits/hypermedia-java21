package io.zeromagic.fullstack.templates;

import static java.lang.StringTemplate.RAW;

import java.net.URI;
import java.util.List;

public abstract class Page implements Framework.Templated {
    @Override
    public StringTemplate template() {
        return RAW."""      
            <!doctype html>
            <html lang="en">
              <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@picocss/pico@1/css/pico.min.css">
                \{libraryInit()}
                <title>\{title()}</title>
                <style>
           
                  body > main {
                    --block-spacing-vertical: calc(var(--spacing)*2)
                  }

                  \{ extraStyles() }
                </style>
              </head>
              <body>
                <nav class="container-fluid">
                  <ul>
                    <li><a href="\{homeLink()}" aria-label="Home">
                      <svg fill="none" height="25" viewBox="0 0 25 25" width="25" xmlns="http://www.w3.org/2000/svg">
                        <title>Home</title>
                        <path clip-rule="evenodd" d="m13.2013 3.14414c-.28-.23806-.6914-.23806-.9714 0l-7.00005 5.95c-.00008.00007-.00016.00014-.00024.00021l-2.99976 2.54975c-.3156.2683-.35398.7416-.08571 1.0572.26826.3156.74158.354 1.05719.0857l1.76426-1.4996v8.4282c0 .9665.7835 1.75 1.75 1.75h12.00001c.9665 0 1.75-.7835 1.75-1.75v-8.4282l1.7643 1.4996c.3156.2683.7889.2299 1.0571-.0857.2683-.3156.2299-.7889-.0857-1.0572l-3-2.54996zm5.7643 6.86826-6.25-5.31248-6.25001 5.31248v9.7032c0 .1381.11193.25.25.25h12.00001c.1381 0 .25-.1119.25-.25z" fill="#000" fill-rule="evenodd"/></svg>
                    </a></li>
                    <li><strong>A shop</strong></li>
                  </ul>
                  <ul>
                    \{navigation()}
                   </ul>
                </nav>
                <main class="container">
                  \{main()}
                </main>
              </body>
            </html>
            """;
    }

    protected abstract String title();
    
    protected String extraStyles() {
        return "";
    }

    protected String homeLink() {
        return "/";
    }

    protected Object libraryInit() {
      return null;
    }

    protected List<NavigationLink> navigation() {
        return List.of(new NavigationLink("Plain", "/plain/"), 
          new NavigationLink("Unpoly", "/unpoly/"),
          new NavigationLink("HTMX", "/htmx/"));
    }

    protected abstract Object main();

    protected static record NavigationLink(String title, String href) implements Framework.Templated {
        protected NavigationLink {
          if (title == null) {
            throw new NullPointerException("title is required");
          }
        }
        @Override
        public StringTemplate template() {
            return RAW."<li><a href='\{href}'>\{title.toUpperCase()}</a></li>";
        }
    }
}