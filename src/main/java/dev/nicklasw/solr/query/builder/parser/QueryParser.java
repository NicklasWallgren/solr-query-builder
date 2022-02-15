package dev.nicklasw.solr.query.builder.parser;

import dev.nicklasw.solr.query.builder.Criteria;
import dev.nicklasw.solr.query.builder.Criteria.Occur;
import dev.nicklasw.solr.query.builder.Node;
import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.parser.processors.BasePredicateProcessor;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryParser {

    private static final String DELIMINATOR = ":";
    private static final String BOOST = "^";
    private static final String WHITESPACE = " ";

    public static String createQueryFor(final Node node) {
        return createQueryFor(node, 0);
    }

    public static String createQueryFor(final Node node, final int position) {
        final StringBuilder query = new StringBuilder();

        if (position > 0) {
            query.append(WHITESPACE).append(node.getOperator()).append(WHITESPACE);
        }

        if (node.hasSiblings()) {
            query.append(node.getOccur());

            if (node.hasParent() || (!node.hasParent() && node.hasOccurNotEqualTo(Occur.SHOULD))) {
                query.append('(');
            }

            int i = 0;
            for (final Node nested : node.getSiblings()) {
                query.append(createQueryFor(nested, i++));
            }

            if ((node.hasParent() || (!node.hasParent() && node.hasOccurNotEqualTo(Occur.SHOULD)))) {
                query.append(')');
            }
        } else {
            query.append(createQueryFragmentFor((Criteria) node));
        }

        return query.toString();
    }

    private static String createQueryFragmentFor(final Criteria part) {
        final StringBuilder queryFragment = new StringBuilder();
        final boolean singeEntryCriteria = (part.getPredicates().size() == 1);

        String fieldName = getNullsafeFieldName(part.getField());
        if (part.getOccur() != Occur.SHOULD) {
            fieldName = part.getOccur() + fieldName;
        }

        if (!StringUtils.isEmpty(fieldName)) {
            queryFragment.append(fieldName);
            queryFragment.append(DELIMINATOR);
        }

        if (!singeEntryCriteria) {
            queryFragment.append("(");
        }

        final CriteriaQueryStringValueProvider valueProvider = new CriteriaQueryStringValueProvider(part);
        while (valueProvider.hasNext()) {
            queryFragment.append(valueProvider.next());
            if (valueProvider.hasNext()) {
                queryFragment.append(BasePredicateProcessor.CRITERIA_VALUE_SEPARATOR);
            }
        }

        if (!singeEntryCriteria) {
            queryFragment.append(")");
        }

        if (!Float.isNaN(part.getBoost())) {
            queryFragment.append(BOOST).append(part.getBoost());
        }

        return queryFragment.toString();
    }

    private static String getNullsafeFieldName(@Nullable final Field field) {
        if (field == null || field.getName() == null) {
            return "";
        }

        return field.getName();
    }

}
