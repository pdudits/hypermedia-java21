package io.zeromagic.fullstack.templates.htmx;

public enum HtmxLibrary {
    INSTANCE;

    public String initCode() {
        return """
            <script src="https://unpkg.com/htmx.org@1.9.2"></script>
            <script>
              htmx.config.useTemplateFragments = true;
            </script>
            """;
    }
}