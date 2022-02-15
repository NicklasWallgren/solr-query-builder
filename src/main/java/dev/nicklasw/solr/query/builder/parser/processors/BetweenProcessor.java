package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import dev.nicklasw.solr.query.builder.Criteria;
import edu.umd.cs.findbugs.annotations.Nullable;

public class BetweenProcessor extends BasePredicateProcessor {

    private static final String RANGE_OPERATOR = " TO ";

    @Override
    public boolean canProcess(@Nullable final Predicate predicate) {
        return predicate != null && Criteria.OperationKey.BETWEEN.getKey().equals(predicate.getKey());
    }

    @Override
    public Object doProcess(final Predicate predicate, final Field field) {
        final Object[] args = (Object[]) predicate.getValue();

        String rangeFragment = (Boolean) args[2] ? "[" : "{";
        rangeFragment += createRangeFragment(args[0], args[1]);
        rangeFragment += (Boolean) args[3] ? "]" : "}";

        return rangeFragment;
    }

    private String createRangeFragment(@Nullable final Object rangeStart, @Nullable final Object rangeEnd) {
        String rangeFragment = "";

        rangeFragment += (rangeStart != null ? filterCriteriaValue(rangeStart) : Criteria.WILDCARD);
        rangeFragment += RANGE_OPERATOR;
        rangeFragment += (rangeEnd != null ? filterCriteriaValue(rangeEnd) : Criteria.WILDCARD);

        return rangeFragment;
    }
}

