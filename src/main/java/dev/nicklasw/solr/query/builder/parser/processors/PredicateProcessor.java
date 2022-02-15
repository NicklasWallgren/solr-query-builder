package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import edu.umd.cs.findbugs.annotations.Nullable;

public interface PredicateProcessor {

    /**
     * Returns true if the {@link Predicate} can be processed by the {@link PredicateProcessor}.
     *
     * @param predicate can be {@code null}
     * @return true if predicate can be processed by this parser
     */
    boolean canProcess(final Predicate predicate);

    /**
     * Create query string representation of given {@link Predicate}.
     *
     * @param predicate can be {@code null}
     * @param field     can be {@code null}
     */
    Object process(final Predicate predicate, @Nullable final Field field);
}
