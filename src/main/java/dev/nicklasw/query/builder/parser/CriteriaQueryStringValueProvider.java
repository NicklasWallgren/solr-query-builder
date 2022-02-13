package dev.nicklasw.query.builder.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dev.nicklasw.query.builder.Criteria;
import dev.nicklasw.query.builder.parser.processors.BetweenProcessor;
import dev.nicklasw.query.builder.parser.processors.DefaultProcessor;
import dev.nicklasw.query.builder.parser.processors.ExpressionProcessor;
import dev.nicklasw.query.builder.parser.processors.FuzzyProcessor;
import dev.nicklasw.query.builder.parser.processors.PredicateProcessor;
import dev.nicklasw.query.builder.parser.processors.RegularExpressionProcessor;
import edu.umd.cs.findbugs.annotations.Nullable;

class CriteriaQueryStringValueProvider implements Iterator<String> {

    private final PredicateProcessor defaultProcessor = new DefaultProcessor();
    private final List<PredicateProcessor> criteriaEntryProcessors = new ArrayList<>();

    {
        criteriaEntryProcessors.add(new ExpressionProcessor());
        criteriaEntryProcessors.add(new BetweenProcessor());
        criteriaEntryProcessors.add(new FuzzyProcessor());
        criteriaEntryProcessors.add(new RegularExpressionProcessor());
    }

    private final Criteria criteria;
    private final Iterator<Predicate> delegate;

    CriteriaQueryStringValueProvider(final Criteria criteria) {
        this.criteria = criteria;
        this.delegate = criteria.getPredicates().iterator();
    }

    @SuppressWarnings({"unchecked", "TypeParameterUnusedInFormals"})
    @Nullable
    private <T> T getPredicateValue(final Predicate predicate) {
        final PredicateProcessor processor = findMatchingProcessor(predicate);

        return (T) processor.process(predicate, criteria.getField());
    }

    private PredicateProcessor findMatchingProcessor(final Predicate predicate) {
        for (final PredicateProcessor processor : criteriaEntryProcessors) {
            if (processor.canProcess(predicate)) {
                return processor;
            }
        }

        return defaultProcessor;
    }

    @Override
    public boolean hasNext() {
        return this.delegate.hasNext();
    }

    @Override
    public String next() {
        final Object o = getPredicateValue(this.delegate.next());
        return o != null ? o.toString() : null;
    }

    @Override
    public void remove() {
        this.delegate.remove();
    }
}
