module org.tlr {
    requires flow.server;
    requires flow.html.components;
    requires flow.data;
    requires vaadin.core;
    requires vaadin.text.field.flow;
    requires vaadin.select.flow;
    requires vaadin.ordered.layout.flow;
    requires io.vidocq.foy.api;
    requires jakarta.servlet;
    // Vaadin's Lookup finds AppShell in a jlink image.
    requires io.vidocq.runtime.extensions.jakartaee.web.vaadin;
    // Vaadin creates the routes by reflection.
    opens org.tlr;
}
