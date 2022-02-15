package dev.nicklasw.solr.query.builder.parser.processors;

import dev.nicklasw.solr.query.builder.Criteria;
import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import edu.umd.cs.findbugs.annotations.Nullable;

public class RegularExpressionProcessor extends BasePredicateProcessor {

    @Override
    public boolean canProcess(@Nullable final Predicate predicate) {
        return predicate != null && Criteria.OperationKey.REGEXP.getKey().equals(predicate.getKey());
    }

    @Override
    protected Object doProcess(final Predicate predicate, final Field field) {
        return "/" + predicate.getValue().toString() + "/";
    }

}
