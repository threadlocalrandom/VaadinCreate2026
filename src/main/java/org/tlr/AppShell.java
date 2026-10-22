package org.tlr;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;

/**
 * Application shell configuration.
 */
@StyleSheet("https://fonts.googleapis.com/css2?family=Fira+Code:wght@400;600&family=Fira+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&display=swap")
@StyleSheet("styles/vidocq.css")
public class AppShell implements AppShellConfigurator {

    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addLink("stylesheet", "https://fonts.googleapis.com/css2?family=Fira+Code:wght@400;600&family=Fira+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&display=swap");
        settings.addLink("stylesheet", "styles/vidocq.css");
    }
}
