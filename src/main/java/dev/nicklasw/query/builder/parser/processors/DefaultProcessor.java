package dev.nicklasw.query.builder.parser.processors;

import dev.nicklasw.query.builder.fields.Field;
import dev.nicklasw.query.builder.parser.Predicate;
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

