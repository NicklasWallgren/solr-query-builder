package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import edu.umd.cs.findbugs.annotations.Nullable;

public class DefaultProcessor extends BasePredicateProcessor {

    @Override
    public boolean canProcess(@Nullable final Predicate predicate) {
        return true;
    }

    @Override
    public Object doProcess(final Predicate predicate, final Field field) {
        return filterCriteriaValue(predicate.getValue());
    }
}

