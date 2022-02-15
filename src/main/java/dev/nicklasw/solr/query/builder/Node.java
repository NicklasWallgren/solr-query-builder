package dev.nicklasw.solr.query.builder;

import static dev.nicklasw.solr.query.builder.Criteria.Occur;
import static dev.nicklasw.solr.query.builder.Criteria.Operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class Node {

    @Nullable
    private Node parent;
    private final List<Node> siblings = new ArrayList<>();
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Operator operator = Operator.OR;
    @Getter
    @Setter(AccessLevel.PACKAGE)
    private Occur occur = Occur.SHOULD;

    public <T extends Node> T and(final T criteria) {
        return add(criteria, Operator.AND);
    }

    public <T extends Node> T or(final T criteria) {
        return add(criteria, Operator.OR);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    public <T extends Node> T connect() {
        final T node = create();
        node.add(this);
        return node;
    }

    public boolean hasParent() {
        return this.parent != null;
    }

    public boolean hasSiblings() {
        return !siblings.isEmpty();
    }

    public boolean hasOccurEqualTo(final Occur occur) {
        return this.occur == occur;
    }

    public boolean hasOccurNotEqualTo(final Occur occur) {
        return this.occur != occur;
    }

    public <T extends Node> List<T> getSiblings() {
        return Collections.unmodifiableList((List<? extends T>) siblings);
    }

    protected void setParent(@Nullable final Node parent) {
        this.parent = parent;
    }

    protected <T extends Node> T add(final T criteria, final Operator operator) {
        criteria.setOperator(operator);

        if (this.siblings.size() > 0) {
            add(criteria);
            return (T) this;
        }

        final T parent = create();
        parent.setParent(this.parent);
        parent.add(this);
        parent.add(criteria);

        return parent;
    }

    protected <T extends Node> T add(final T node) {
        node.setParent(this);
        this.siblings.add(node);
        return (T) this;
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    protected abstract <T extends Node> T create();

}
