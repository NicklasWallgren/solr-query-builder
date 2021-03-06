package dev.nicklasw.solr.query.builder.parser;

import dev.nicklasw.solr.query.builder.Criteria;

public class Predicate {

    private final String key;
    private final Object value;

    public Predicate(final Criteria.OperationKey key, final Object value) {
        this(key.getKey(), value);
    }

    public Predicate(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}

