package org.tlr;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
/**
 * Main view of the application.
 */
@Route("")
public class MainView extends VerticalLayout {

    private final GreetService greetService = new GreetService();

    public MainView() {
        H1 heading = new H1("Welcome to Vaadin Create 2026");
        Paragraph subtitle = new Paragraph("A simple Vaadin application using the latest Vaadin 25.");

        TextField nameField = new TextField("Your name");
        nameField.setPlaceholder("Enter your name");

        Paragraph greeting = new Paragraph();
        greeting.setText(greetService.greet(""));

        nameField.addValueChangeListener(event -> {
            String name = event.getValue();
            greeting.setText(greetService.greet(name));
        });

        VerticalLayout contentLayout = new VerticalLayout(heading, subtitle, nameField, greeting);
        contentLayout.setPadding(true);
        contentLayout.setSpacing(true);
        contentLayout.setMaxWidth("600px");

        FlexLayout mainLayout = new FlexLayout(contentLayout);
        mainLayout.setFlexGrow(1, contentLayout);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setSizeFull();

        add(mainLayout);
    }
}
