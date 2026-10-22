package org.tlr;

import java.util.Objects;

/**
 * Selectable item representing a Vidocq component.
 * Uses the component name as both its key and display name.
 */
public record ComponentItem(
        String key,
        String name,
        String id,
        String role,
        String repository,
        String docUrl,
        String logo,
        String preambleHtml,
        String originHtml
) implements Comparable<ComponentItem> {

    public ComponentItem(String name, String id, String role, String repository, String docUrl, String logo, String preambleHtml, String originHtml) {
        this(name, name, id, role, repository, docUrl, logo, preambleHtml, originHtml);
    }

    public String getKey() {
        return key != null ? key : name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getRepository() {
        return repository;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public String getLogo() {
        return logo;
    }

    public String getPreambleHtml() {
        return preambleHtml;
    }

    public String getOriginHtml() {
        return originHtml;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentItem that)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public int compareTo(ComponentItem other) {
        if (other == null || other.name == null) return 1;
        if (this.name == null) return -1;
        return this.name.compareToIgnoreCase(other.name);
    }
}
