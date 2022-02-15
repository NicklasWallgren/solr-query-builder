package dev.nicklasw.solr.query.builder.parser.processors;

import java.util.Set;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import dev.nicklasw.solr.query.builder.parser.converter.ConverterProcessor;
import edu.umd.cs.findbugs.annotations.Nullable;
import org.apache.commons.lang3.StringUtils;

public abstract class BasePredicateProcessor implements PredicateProcessor {

    public static final String CRITERIA_VALUE_SEPARATOR = " ";

    private static final String DOUBLE_QUOTE = "\"";

    private static final Set<String> BOOLEAN_OPERATORS = Set.of("NOT", "AND", "OR");

    private static final String[] RESERVED_CHARS = {DOUBLE_QUOTE, "+", "-", "&&", "||", "!", "(", ")", "{", "}", "[", "]",
        "^", "~", "*", "?", ":", "\\"};

    private static final String[] RESERVED_CHARS_REPLACEMENT = {"\\" + DOUBLE_QUOTE, "\\+", "\\-", "\\&\\&", "\\|\\|", "\\!",
        "\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\^", "\\~", "\\*", "\\?", "\\:", "\\\\"};


    private static final ConverterProcessor converterProcessor = new ConverterProcessor();

    @Override
    public Object process(final Predicate predicate, final Field field) {
        if (predicate == null) {
            return null;
        }

        return doProcess(predicate, field);
    }

    protected Object filterCriteriaValue(final Object criteriaValue) {
        if (!(criteriaValue instanceof String)) {
            return converterProcessor.convert(criteriaValue, String.class);
        }

        final String value = escapeCriteriaValue((String) criteriaValue);
        return processWhiteSpaces(value);
    }

    private String escapeCriteriaValue(final String criteriaValue) {
        return StringUtils.replaceEach(criteriaValue, RESERVED_CHARS, RESERVED_CHARS_REPLACEMENT);
    }

    private String processWhiteSpaces(final String criteriaValue) {
        if (StringUtils.contains(criteriaValue, CRITERIA_VALUE_SEPARATOR) || BOOLEAN_OPERATORS.contains(criteriaValue)) {
            return DOUBLE_QUOTE + criteriaValue + DOUBLE_QUOTE;
        }
        return criteriaValue;
    }

    @Nullable
    protected abstract Object doProcess(final Predicate predicate, final Field field);
}
