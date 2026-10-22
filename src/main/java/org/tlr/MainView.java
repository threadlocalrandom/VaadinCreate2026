package org.tlr;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import java.util.List;

/**
 * Main view of the application, styled following the Vidocq documentation design.
 */
@Route("")
@StyleSheet("https://fonts.googleapis.com/css2?family=Fira+Code:wght@400;600&family=Fira+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,400&display=swap")
@StyleSheet("styles/vidocq.css")
public class MainView extends VerticalLayout {

    private final GreetService greetService = new GreetService();
    private final ComponentService componentService = new ComponentService();

    private final Div preamble;
    private final Div section1;
    private final Select<ComponentItem> componentSelect;

    public MainView() {
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        addClassName("vidocq-app-container");

        // 1. Navbar
        Header navbar = createNavbar();

        // 2. Main content container
        Div mainContent = new Div();
        mainContent.addClassName("vidocq-main-content");
        setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, mainContent);

        // 2a. Hero section
        Div hero = createHeroSection();

        // 2b. Interactive Greeting Card
        Div card = createGreetingCard();

        // 2c. Component Selector containing Preamble & Section 1
        this.preamble = createPreamble();
        this.section1 = createSection1();
        this.componentSelect = new Select<>();
        Div componentSelector = createComponentSelector(preamble, section1, componentSelect);
        componentSelector.add(preamble, section1);

        // 2d. Features Grid
        Div features = createFeaturesGrid();

        mainContent.add(hero, card, componentSelector, features);

        // 3. Footer
        Footer footer = createFooter();

        add(navbar, mainContent, footer);
    }

    private Header createNavbar() {
        Header header = new Header();
        header.addClassName("vidocq-navbar");
        header.getStyle().set("display", "flex");
        header.getStyle().set("justify-content", "center");
        header.getStyle().set("align-items", "center");
        header.getStyle().set("width", "100%");

        Div inner = new Div();
        inner.addClassName("vidocq-navbar-inner");
        inner.getStyle().set("display", "flex");
        inner.getStyle().set("justify-content", "center");
        inner.getStyle().set("align-items", "center");
        inner.getStyle().set("width", "100%");

        Anchor brand = new Anchor("https://vidocq.dev/");
        brand.addClassName("vidocq-navbar-brand");
        brand.getStyle().set("display", "flex");
        brand.getStyle().set("justify-content", "center");
        brand.getStyle().set("align-items", "center");

        Image logo = new Image("images/vidocq-logo.png", "Vidocq Logo");
        logo.addClassName("vidocq-navbar-logo");
        logo.setWidth("96px");
        logo.setHeight("96px");

        brand.add(logo);

        inner.add(brand);
        header.add(inner);
        return header;
    }

    private Div createHeroSection() {
        Div hero = new Div();
        hero.addClassName("vidocq-hero");
        hero.getStyle().set("text-align", "center");
        hero.getStyle().set("display", "flex");
        hero.getStyle().set("flex-direction", "column");
        hero.getStyle().set("align-items", "center");
        hero.getStyle().set("justify-content", "center");

        H1 heading = new H1("Vaadin Create 2026");
        heading.addClassName("vidocq-hero-title");
        heading.getStyle().set("text-align", "center");

        Paragraph subtitle = new Paragraph("A modern, lightweight Vaadin 25 application powered by the Vidocq runtime, pure Java 25, virtual threads, and Foy Servlet 6.1.");
        subtitle.addClassName("vidocq-hero-subtitle");
        subtitle.getStyle().set("text-align", "center");

        hero.add(heading, subtitle);
        return hero;
    }

    private Div createPreamble() {
        Div preamble = new Div();
        preamble.setId("preamble");
        preamble.addClassNames("vidocq-card", "vidocq-preamble", "preamble");
        preamble.getStyle().set("display", "flex");
        preamble.getStyle().set("flex-direction", "column");
        preamble.getStyle().set("align-items", "center");
        preamble.getStyle().set("justify-content", "center");
        preamble.getStyle().set("text-align", "center");
        preamble.getStyle().set("width", "100%");
        return preamble;
    }

    private Div createSection1() {
        Div section1 = new Div();
        section1.setId("section1");
        section1.addClassNames("vidocq-card", "vidocq-sect1", "sect1");
        return section1;
    }

    private Div createGreetingCard() {
        Div card = new Div();
        card.addClassName("vidocq-card");

        Div cardHeader = new Div();
        cardHeader.addClassName("vidocq-card-header");

        H2 cardTitle = new H2("Interactive Greeting Service");
        cardTitle.addClassName("vidocq-card-title");
        cardHeader.add(cardTitle);

        TextField nameField = new TextField("Your name");
        nameField.setPlaceholder("Enter your name here...");
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        nameField.addClassName("vidocq-input-field");

        Div admonition = new Div();
        admonition.addClassName("vidocq-admonition");

        Span icon = new Span("✦");
        icon.addClassName("vidocq-admonition-icon");

        Paragraph greeting = new Paragraph(greetService.greet(""));
        greeting.addClassName("vidocq-admonition-text");

        admonition.add(icon, greeting);

        nameField.addValueChangeListener(event -> {
            String name = event.getValue();
            greeting.setText(greetService.greet(name));
        });

        card.add(cardHeader, nameField, admonition);
        return card;
    }

    private Div createComponentSelector(Div preamble, Div section1, Select<ComponentItem> select) {
        Div selector = new Div();
        selector.addClassName("vidocq-card");
        selector.addClassName("vidocq-component-selector");
        selector.getStyle().set("display", "flex");
        selector.getStyle().set("flex-direction", "column");
        selector.getStyle().set("justify-content", "center");
        selector.getStyle().set("align-items", "center");
        selector.getStyle().set("gap", "1.5rem");

        select.addClassName("vidocq-component-select");
        select.setPlaceholder("Choose a component...");
        select.setEmptySelectionAllowed(true);
        select.setEmptySelectionCaption("Choose a component...");
        select.setItemLabelGenerator(item -> item != null ? item.getName() : "");

        List<ComponentItem> items = componentService.getComponents();
        select.setItems(items);

        select.addValueChangeListener(event -> {
            ComponentItem selected = event.getValue();
            updateComponentDetails(selected, preamble, section1);
        });

        selector.add(select);
        return selector;
    }

    Div getPreambleDiv() {
        return preamble;
    }

    Div getSection1Div() {
        return section1;
    }

    Select<ComponentItem> getComponentSelect() {
        return componentSelect;
    }

    void updateComponentDetails(ComponentItem item, Div preamble, Div section1) {
        if (item == null) {
            preamble.removeAll();
            preamble.getElement().setProperty("innerHTML", "");
            section1.removeAll();
            section1.getElement().setProperty("innerHTML", "");
            return;
        }

        String preambleHtml = item.getPreambleHtml();
        if (preambleHtml != null && !preambleHtml.isBlank()) {
            preamble.getElement().setProperty("innerHTML", formatHtml(preambleHtml));
        } else {
            preamble.removeAll();
            preamble.getElement().setProperty("innerHTML", "");
        }

        String originHtml = item.getOriginHtml();
        if (originHtml != null && !originHtml.isBlank()) {
            String cleanOrigin = cleanOriginHtml(originHtml);
            section1.getElement().setProperty("innerHTML", formatHtml(cleanOrigin));
        } else {
            section1.removeAll();
            section1.getElement().setProperty("innerHTML", "");
        }
    }

    String cleanOriginHtml(String html) {
        if (html == null) return "";
        String trimmed = html.strip();
        if (trimmed.startsWith("<div class=\"sect1\">") && trimmed.endsWith("</div>")) {
            trimmed = trimmed.substring("<div class=\"sect1\">".length(), trimmed.length() - "</div>".length()).strip();
        }
        return trimmed;
    }

    String formatHtml(String html) {
        if (html == null) return "";
        return html.replace("href=\"../../", "href=\"https://doc.vidocq.dev/");
    }

    private Div createFeaturesGrid() {
        Div grid = new Div();
        grid.addClassName("vidocq-features-grid");

        grid.add(createFeatureItem(
                "⚡ Vidocq Runtime",
                "MicroProfile & Jakarta EE runtime in pure Java 25 with virtual threads and compile-time code generation."
        ));

        grid.add(createFeatureItem(
                "🚀 Foy Servlet 6.1",
                "Build-time Jakarta Servlet container orchestrating Vaadin Flow with sub-second startup."
        ));

        grid.add(createFeatureItem(
                "🎨 Vaadin 25 & Lumo",
                "Full-stack Java web framework with reactive UI components and modern, accessible theming."
        ));

        return grid;
    }

    private Div createFeatureItem(String titleText, String descText) {
        Div item = new Div();
        item.addClassName("vidocq-feature-item");

        H3 title = new H3(titleText);
        title.addClassName("vidocq-feature-title");

        Paragraph desc = new Paragraph(descText);
        desc.addClassName("vidocq-feature-desc");

        item.add(title, desc);
        return item;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        footer.addClassName("vidocq-footer");

        Div inner = new Div();
        inner.addClassName("vidocq-footer-inner");
        inner.getStyle().set("display", "flex");
        inner.getStyle().set("flex-direction", "column");
        inner.getStyle().set("align-items", "center");
        inner.getStyle().set("justify-content", "center");
        inner.getStyle().set("gap", "0.85rem");

        Span copy = new Span("© 2026 Vidocq Team — MicroProfile runtime for Java 25, AI-augmented development");
        copy.addClassName("vidocq-footer-copy");

        Span meta = new Span("Vaadin Create 2026 · Pure Java 25 · Virtual Threads · Foy Servlet");
        meta.addClassName("vidocq-footer-meta");

        inner.add(copy, meta);
        footer.add(inner);
        return footer;
    }
}
