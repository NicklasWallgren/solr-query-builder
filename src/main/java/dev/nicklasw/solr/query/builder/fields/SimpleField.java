package dev.nicklasw.solr.query.builder.fields;

import java.util.Objects;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleField implements Field {

    private final String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SimpleField)) {
            return false;
        }
        SimpleField that = (SimpleField) other;
        return Objects.equals(this.name, that.name);
    }

}
