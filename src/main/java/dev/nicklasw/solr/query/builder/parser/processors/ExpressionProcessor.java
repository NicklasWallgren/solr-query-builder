package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import dev.nicklasw.solr.query.builder.Criteria;
import edu.umd.cs.findbugs.annotations.Nullable;

public class ExpressionProcessor extends BasePredicateProcessor {

    @Override
    public boolean canProcess(@Nullable final Predicate predicate) {
        return predicate != null && Criteria.OperationKey.EXPRESSION.getKey().equals(predicate.getKey());
    }

    @Override
    public Object doProcess(final Predicate predicate, final Field field) {
        return predicate.getValue().toString();
    }
}
