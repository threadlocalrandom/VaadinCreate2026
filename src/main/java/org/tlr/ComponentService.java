package org.tlr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service responsible for loading component metadata and documentation from classpath resources.
 */
public class ComponentService {

    private final List<ComponentItem> components;
    private final Map<String, ComponentItem> componentByName;
    private final Map<String, ComponentItem> componentById;

    public ComponentService() {
        this.components = loadComponents();
        this.componentByName = new HashMap<>();
        this.componentById = new HashMap<>();

        for (ComponentItem item : this.components) {
            this.componentByName.put(item.name(), item);
            this.componentById.put(item.id(), item);
        }
    }

    /**
     * Returns the list of all available selectable component items.
     * Each item uses the component name as its name and key.
     */
    public List<ComponentItem> getComponents() {
        return Collections.unmodifiableList(components);
    }

    /**
     * Looks up a component item by its name (which also acts as its key).
     */
    public ComponentItem getByName(String name) {
        if (name == null) return null;
        return componentByName.get(name);
    }

    /**
     * Looks up a component item by its ID.
     */
    public ComponentItem getById(String id) {
        if (id == null) return null;
        return componentById.get(id);
    }

    private List<ComponentItem> loadComponents() {
        List<ComponentItem> list = new ArrayList<>();
        String indexJson = readResource("/components/index.json");
        if (indexJson == null || indexJson.isBlank()) {
            indexJson = readResource("/components.json");
        }

        if (indexJson != null && !indexJson.isBlank()) {
            List<Map<String, String>> entries = new SimpleJsonParser(indexJson).parseArrayOfObjects();
            for (Map<String, String> entry : entries) {
                String id = entry.getOrDefault("id", "");
                String name = entry.getOrDefault("name", id);
                String role = entry.getOrDefault("role", "");
                String repository = entry.getOrDefault("repository", "");
                String docUrl = entry.getOrDefault("docUrl", "");
                String logo = entry.getOrDefault("logo", "images/vidocq-logo.png");

                String preambleHtml = "";
                String originHtml = "";

                // Attempt to load detailed HTML/JSON for this component
                String detailedJson = readResource("/components/" + id + ".json");
                if (detailedJson != null && !detailedJson.isBlank()) {
                    Map<String, String> detailed = new SimpleJsonParser(detailedJson).parseObject();
                    preambleHtml = detailed.getOrDefault("preambleHtml", "");
                    originHtml = detailed.getOrDefault("originHtml", "");
                }

                // If preamble/origin not found in JSON, check for direct HTML file
                if (preambleHtml.isBlank() && originHtml.isBlank()) {
                    String fullHtml = readResource("/components/" + id + ".html");
                    if (fullHtml != null && !fullHtml.isBlank()) {
                        preambleHtml = extractPreamble(fullHtml);
                        originHtml = extractOrigin(fullHtml);
                    }
                }

                ComponentItem item = new ComponentItem(
                        name, // key
                        name, // name
                        id,
                        role,
                        repository,
                        docUrl,
                        logo,
                        preambleHtml,
                        originHtml
                );
                list.add(item);
            }
        }

        return list;
    }

    private String readResource(String path) {
        String[] candidatePaths = {
                path,
                path.startsWith("/") ? "/META-INF/resources" + path : "/META-INF/resources/" + path
        };

        for (String candidate : candidatePaths) {
            try (InputStream is = getClass().getResourceAsStream(candidate)) {
                if (is != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        return sb.toString();
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return null;
    }

    private String extractPreamble(String fullHtml) {
        Pattern pattern = Pattern.compile("(?s)<div id=\"preamble\"[^>]*>(.*?)</div>\\s*<div class=\"sect1\"");
        Matcher matcher = pattern.matcher(fullHtml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private String extractOrigin(String fullHtml) {
        Pattern pattern = Pattern.compile("(?s)(<div class=\"sect1\">\\s*<h2 id=\"[^\"]*origin[^\"]*\">.*?</div>\\s*</div>)");
        Matcher matcher = pattern.matcher(fullHtml);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    private static class SimpleJsonParser {
        private final String src;
        private int pos = 0;

        SimpleJsonParser(String src) {
            this.src = src != null ? src.stripLeading() : "";
            // Strip UTF-8 BOM if present
            if (this.src.startsWith("\uFEFF")) {
                this.pos = 1;
            }
        }

        private void skipWhitespace() {
            while (pos < src.length() && Character.isWhitespace(src.charAt(pos))) {
                pos++;
            }
        }

        private char peek() {
            skipWhitespace();
            if (pos >= src.length()) return '\0';
            return src.charAt(pos);
        }

        private char nextChar() {
            skipWhitespace();
            if (pos >= src.length()) return '\0';
            return src.charAt(pos++);
        }

        List<Map<String, String>> parseArrayOfObjects() {
            List<Map<String, String>> list = new ArrayList<>();
            if (peek() != '[') return list;
            nextChar(); // consume '['
            while (true) {
                skipWhitespace();
                char c = peek();
                if (c == ']' || c == '\0') {
                    if (c == ']') nextChar();
                    break;
                }
                if (c == ',') {
                    nextChar();
                    continue;
                }
                if (c == '{') {
                    list.add(parseObject());
                } else {
                    pos++;
                }
            }
            return list;
        }

        Map<String, String> parseObject() {
            Map<String, String> map = new HashMap<>();
            if (peek() != '{') return map;
            nextChar(); // consume '{'
            while (true) {
                skipWhitespace();
                char c = peek();
                if (c == '}' || c == '\0') {
                    if (c == '}') nextChar();
                    break;
                }
                if (c == ',') {
                    nextChar();
                    continue;
                }
                if (c == '"') {
                    String key = parseString();
                    skipWhitespace();
                    if (peek() == ':') {
                        nextChar(); // consume ':'
                        skipWhitespace();
                        String val = "";
                        if (peek() == '"') {
                            val = parseString();
                        } else if (peek() == '{') {
                            parseObject();
                        } else if (peek() == '[') {
                            skipArray();
                        } else {
                            val = parseLiteral();
                        }
                        map.put(key, val);
                    }
                } else {
                    pos++;
                }
            }
            return map;
        }

        private void skipArray() {
            if (peek() != '[') return;
            nextChar();
            int depth = 1;
            while (pos < src.length() && depth > 0) {
                char c = src.charAt(pos++);
                if (c == '"') {
                    pos--;
                    parseString();
                } else if (c == '[') {
                    depth++;
                } else if (c == ']') {
                    depth--;
                }
            }
        }

        private String parseLiteral() {
            skipWhitespace();
            int start = pos;
            while (pos < src.length()) {
                char c = src.charAt(pos);
                if (c == ',' || c == '}' || c == ']' || Character.isWhitespace(c)) {
                    break;
                }
                pos++;
            }
            return src.substring(start, pos);
        }

        private String parseString() {
            if (peek() != '"') return "";
            nextChar(); // consume leading '"'
            StringBuilder sb = new StringBuilder();
            while (pos < src.length()) {
                char c = src.charAt(pos++);
                if (c == '"') {
                    break;
                }
                if (c == '\\' && pos < src.length()) {
                    char esc = src.charAt(pos++);
                    switch (esc) {
                        case '"' -> sb.append('"');
                        case '\\' -> sb.append('\\');
                        case '/' -> sb.append('/');
                        case 'b' -> sb.append('\b');
                        case 'f' -> sb.append('\f');
                        case 'n' -> sb.append('\n');
                        case 'r' -> sb.append('\r');
                        case 't' -> sb.append('\t');
                        case 'u' -> {
                            if (pos + 4 <= src.length()) {
                                String hex = src.substring(pos, pos + 4);
                                try {
                                    sb.append((char) Integer.parseInt(hex, 16));
                                    pos += 4;
                                } catch (NumberFormatException e) {
                                    sb.append('\\').append('u');
                                }
                            } else {
                                sb.append('\\').append('u');
                            }
                        }
                        default -> sb.append(esc);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }
}
