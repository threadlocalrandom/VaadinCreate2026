package org.tlr;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.select.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentSelectionTest {

    private ComponentService componentService;
    private MainView mainView;

    @BeforeEach
    void setUp() {
        componentService = new ComponentService();
        mainView = new MainView();
    }

    @Test
    @DisplayName("ComponentService loads all 20 components with valid metadata and content")
    void testComponentLoadingAndProperties() {
        List<ComponentItem> items = componentService.getComponents();

        assertNotNull(items, "Component list should not be null");
        assertEquals(20, items.size(), "Should contain exactly 20 components");

        for (ComponentItem item : items) {
            assertNotNull(item.getName(), "Component name should not be null");
            assertNotNull(item.getKey(), "Component key should not be null");
            assertEquals(item.getName(), item.getKey(), "Component key should equal component name");
            assertNotNull(item.getId(), "Component ID should not be null");
            assertFalse(item.getName().isBlank(), "Component name should not be blank");
            assertFalse(item.getId().isBlank(), "Component ID should not be blank");
            assertNotNull(item.getPreambleHtml(), "Preamble HTML should not be null for " + item.getName());
            assertNotNull(item.getOriginHtml(), "Origin HTML should not be null for " + item.getName());
            assertFalse(item.getPreambleHtml().isBlank(), "Preamble HTML should not be blank for " + item.getName());
            assertFalse(item.getOriginHtml().isBlank(), "Origin HTML should not be blank for " + item.getName());
        }

        ComponentItem vidocq = componentService.getByName("Vidocq Runtime");
        assertNotNull(vidocq, "Vidocq Runtime should be present");
        assertTrue(vidocq.getPreambleHtml().contains("runtime MicroProfile"), "Preamble should contain runtime MicroProfile description");
        assertTrue(vidocq.getOriginHtml().contains("Eugène-François Vidocq"), "Origin should contain historical context");

        ComponentItem vauban = componentService.getByName("Vauban");
        assertNotNull(vauban, "Vauban should be present");
        assertTrue(vauban.getPreambleHtml().contains("CDI 4.1"), "Preamble should contain CDI 4.1 description");
        assertTrue(vauban.getOriginHtml().contains("Sébastien Le Prestre"), "Origin should contain historical context");
    }

    @Test
    @DisplayName("ComponentService handles lookup by name, key, and id, including edge cases")
    void testComponentServiceLookups() {
        ComponentItem item = componentService.getByName("Chappe");
        assertNotNull(item);
        assertEquals("chappe", item.getId());
        assertEquals("Chappe", item.getName());
        assertEquals("Chappe", item.getKey());

        ComponentItem byId = componentService.getById("chappe");
        assertEquals(item, byId);

        assertNull(componentService.getByName(null));
        assertNull(componentService.getByName("NonExistentComponent"));
        assertNull(componentService.getById(null));
        assertNull(componentService.getById("non-existent-id"));
    }

    @Test
    @DisplayName("HTML formatting replaces relative documentation paths with absolute doc.vidocq.dev URLs")
    void testFormatHtml() {
        String input = "<p>See <a href=\"../../chappe/0.3.0/index.html\">Chappe</a> and <a href=\"../../vauban/0.3.0/index.html\">Vauban</a>.</p>";
        String formatted = mainView.formatHtml(input);

        assertNotNull(formatted);
        assertFalse(formatted.contains("href=\"../../"));
        assertTrue(formatted.contains("href=\"https://doc.vidocq.dev/chappe/0.3.0/index.html\""));
        assertTrue(formatted.contains("href=\"https://doc.vidocq.dev/vauban/0.3.0/index.html\""));

        assertEquals("", mainView.formatHtml(null));
        assertEquals("", mainView.formatHtml(""));
    }

    @Test
    @DisplayName("Origin HTML cleaning strips outer sect1 wrapper div")
    void testCleanOriginHtml() {
        String wrapped = "<div class=\"sect1\">\n  <h2>Origin of the name</h2>\n  <p>Some historical facts.</p>\n</div>";
        String cleaned = mainView.cleanOriginHtml(wrapped);

        assertFalse(cleaned.startsWith("<div class=\"sect1\">"));
        assertFalse(cleaned.endsWith("</div>"));
        assertTrue(cleaned.contains("<h2>Origin of the name</h2>"));
        assertTrue(cleaned.contains("<p>Some historical facts.</p>"));

        assertEquals("", mainView.cleanOriginHtml(null));
        assertEquals("", mainView.cleanOriginHtml(""));
    }

    @Test
    @DisplayName("Selecting a component populates preamble and section1 divs accordingly")
    void testUpdateComponentDetailsWithComponent() {
        Div preamble = new Div();
        Div section1 = new Div();

        ComponentItem vidocq = componentService.getByName("Vidocq Runtime");
        assertNotNull(vidocq);

        mainView.updateComponentDetails(vidocq, preamble, section1);

        String preambleHtml = (String) preamble.getElement().getPropertyRaw("innerHTML");
        String section1Html = (String) section1.getElement().getPropertyRaw("innerHTML");

        assertNotNull(preambleHtml, "Preamble innerHTML should be populated");
        assertNotNull(section1Html, "Section1 innerHTML should be populated");
        assertFalse(preambleHtml.isBlank(), "Preamble innerHTML should not be blank");
        assertFalse(section1Html.isBlank(), "Section1 innerHTML should not be blank");

        assertTrue(preambleHtml.contains("runtime MicroProfile"));
        assertTrue(section1Html.contains("Eugène-François Vidocq"));
    }

    @Test
    @DisplayName("Deselection (null component) clears preamble and section1 divs")
    void testUpdateComponentDetailsWithNullClearsDivs() {
        Div preamble = new Div();
        Div section1 = new Div();

        ComponentItem cassini = componentService.getByName("Cassini");
        assertNotNull(cassini);

        // Select component first
        mainView.updateComponentDetails(cassini, preamble, section1);
        assertNotNull(preamble.getElement().getPropertyRaw("innerHTML"));
        assertNotNull(section1.getElement().getPropertyRaw("innerHTML"));

        // Deselect
        mainView.updateComponentDetails(null, preamble, section1);

        assertEquals("", preamble.getElement().getPropertyRaw("innerHTML"));
        assertEquals("", section1.getElement().getPropertyRaw("innerHTML"));
        assertEquals(0, preamble.getComponentCount());
        assertEquals(0, section1.getComponentCount());
    }

    @Test
    @DisplayName("Component with blank preamble or origin HTML updates containers cleanly")
    void testUpdateComponentDetailsWithEmptyHtml() {
        Div preamble = new Div();
        Div section1 = new Div();

        ComponentItem blankItem = new ComponentItem(
                "BlankItem", "BlankItem", "blank", "Role", "Repo", "doc", "logo", "", ""
        );

        mainView.updateComponentDetails(blankItem, preamble, section1);

        assertEquals("", preamble.getElement().getPropertyRaw("innerHTML"));
        assertEquals("", section1.getElement().getPropertyRaw("innerHTML"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Chappe", "Vauban", "Cassini", "Champollion", "Foy", "Mansart"})
    @DisplayName("Selecting various foundational components correctly updates details")
    void testComponentSelectionAcrossMultipleBricks(String componentName) {
        Div preamble = new Div();
        Div section1 = new Div();

        ComponentItem item = componentService.getByName(componentName);
        assertNotNull(item, "Component " + componentName + " should exist");

        mainView.updateComponentDetails(item, preamble, section1);

        String preambleHtml = (String) preamble.getElement().getPropertyRaw("innerHTML");
        String section1Html = (String) section1.getElement().getPropertyRaw("innerHTML");

        assertNotNull(preambleHtml, "Preamble should be populated for " + componentName);
        assertNotNull(section1Html, "Section1 should be populated for " + componentName);
        assertFalse(preambleHtml.contains("href=\"../../"), "Relative links should be formatted");
        assertFalse(section1Html.contains("<div class=\"sect1\">"), "Wrapper div should be stripped");
    }

    @Test
    @DisplayName("UI Select component value change updates preamble and section1 in MainView")
    void testMainViewSelectValueChangeListener() {
        Div preamble = mainView.getPreambleDiv();
        Div section1 = mainView.getSection1Div();
        Select<ComponentItem> select = mainView.getComponentSelect();

        assertNotNull(preamble, "Preamble div should exist in MainView");
        assertNotNull(section1, "Section1 div should exist in MainView");
        assertNotNull(select, "Component Select should exist in MainView");

        // Initially empty
        assertNull(preamble.getElement().getPropertyRaw("innerHTML"));
        assertNull(section1.getElement().getPropertyRaw("innerHTML"));
        assertEquals("flex", preamble.getStyle().get("display"));
        assertEquals("center", preamble.getStyle().get("align-items"));
        assertEquals("center", preamble.getStyle().get("justify-content"));

        // Select Vauban
        ComponentItem vauban = componentService.getByName("Vauban");
        select.setValue(vauban);

        String preambleHtml = (String) preamble.getElement().getPropertyRaw("innerHTML");
        String section1Html = (String) section1.getElement().getPropertyRaw("innerHTML");
        assertNotNull(preambleHtml);
        assertNotNull(section1Html);
        assertTrue(preambleHtml.contains("CDI 4.1"));
        assertTrue(section1Html.contains("Sébastien Le Prestre"));

        // Switch selection to Cassini
        ComponentItem cassini = componentService.getByName("Cassini");
        select.setValue(cassini);

        preambleHtml = (String) preamble.getElement().getPropertyRaw("innerHTML");
        section1Html = (String) section1.getElement().getPropertyRaw("innerHTML");
        assertNotNull(preambleHtml);
        assertNotNull(section1Html);
        assertTrue(preambleHtml.contains("REST Client") || preambleHtml.contains("Cassini") || preambleHtml.contains("Jakarta REST"));
        assertTrue(section1Html.contains("Cassini"));

        // Clear selection
        select.setValue(null);
        assertEquals("", preamble.getElement().getPropertyRaw("innerHTML"));
        assertEquals("", section1.getElement().getPropertyRaw("innerHTML"));
    }
}
