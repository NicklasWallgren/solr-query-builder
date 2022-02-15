package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.Criteria.OperationKey;
import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import edu.umd.cs.findbugs.annotations.Nullable;

public class FuzzyProcessor extends BasePredicateProcessor {

    @Override
    public boolean canProcess(@Nullable final Predicate predicate) {
        return predicate != null && OperationKey.FUZZY.getKey().equals(predicate.getKey());
    }

    @Override
    protected Object doProcess(final Predicate predicate, final Field field) {
        final Object[] args = (Object[]) predicate.getValue();
        final Float distance = (Float) args[1];

        return filterCriteriaValue(args[0]) + "~" + (distance.isNaN() ? "" : distance);
    }
}

