package dev.nicklasw.solr.query.builder;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import dev.nicklasw.solr.query.builder.fields.Field;
import dev.nicklasw.solr.query.builder.fields.SimpleField;
import dev.nicklasw.solr.query.builder.parser.Predicate;
import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
public class Criteria extends Node {

    public static final String WILDCARD = "*";

    @Nullable
    private Field field;
    private final Set<Predicate> predicates = new LinkedHashSet<>();
    private float boost = Float.NaN;

    public Criteria(final Field field) {
        this.field = field;
    }

    /**
     * Static factory method to create a new {@link Criteria} for field with given name.
     *
     * @param fieldName must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public static Criteria where(@NonNull final String fieldName) {
        return where(new SimpleField(fieldName));
    }

    /**
     * Static factory method to create a new {@link Criteria} for provided field.
     *
     * @param field must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public static Criteria where(@NonNull final Field field) {
        return new Criteria(field);
    }

    /**
     * Creates new {@link Predicate} allowing native solr expressions.
     *
     * @param s must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public Criteria expression(@NonNull final String s) {
        predicates.add(new Predicate(OperationKey.EXPRESSION, s));
        return this;
    }

    /**
     * Creates new {@link Predicate} without any wildcards. Strings with blanks will be escaped
     * {@code "string\ with\ blank"}
     *
     * @param o must not be {@literal null}
     * @return the {@link Criteria}
     */
    public Criteria is(@NonNull final Object o) {
        predicates.add(new Predicate(OperationKey.EQUALS, o));
        return this;
    }

    /**
     * Creates new {@link Predicate} for {@code RANGE [lowerBound TO upperBound]}.
     *
     * @param lowerBound can be {@literal null}. A null value will be handled as a wildcard.
     * @param upperBound can be {@literal null}. A null value will be handled as a wildcard.
     * @return the {@link Criteria}
     */
    public Criteria between(@Nullable final Object lowerBound, @Nullable final Object upperBound) {
        return between(lowerBound, upperBound, true, true);
    }

    /**
     * Crates new {@link Predicate} for {@code RANGE [lowerBound TO upperBound]}.
     *
     * @param lowerBound        can be {@literal null}. A null value will be handled as a wildcard.
     * @param upperBound        can be {@literal null}. A null value will be handled as a wildcard.
     * @param includeLowerBound to include the lower bound
     * @param includeUpperBound to include the upper bound
     * @return the {@link Criteria}
     */
    public Criteria between(@Nullable final Object lowerBound, @Nullable final Object upperBound, final boolean includeLowerBound,
                            final boolean includeUpperBound) {
        predicates.add(new Predicate(OperationKey.BETWEEN,
            new Object[] {lowerBound, upperBound, includeLowerBound, includeUpperBound}));
        return this;
    }

    /**
     * Creates new {@link Predicate} with trailing {@code ~}.
     *
     * @param s must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public Criteria fuzzy(final String s) {
        return fuzzy(s, Float.NaN);
    }

    /**
     * Creates new {@link Predicate} with trailing {@code ~} followed by levensteinDistance.
     *
     * @param s                   must not be {@literal null}.
     * @param levenshteinDistance must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public Criteria fuzzy(@NonNull final String s, final float levenshteinDistance) {
        if (!Float.isNaN(levenshteinDistance) && (levenshteinDistance < 0 || levenshteinDistance > 1)) {
            throw new IllegalArgumentException("Levenshtein Distance has to be within its bounds (0.0 - 1.0).");
        }
        predicates.add(new Predicate(OperationKey.FUZZY, new Object[] {s, Float.valueOf(levenshteinDistance)}));
        return this;
    }

    /**
     * Creates new {@link Predicate} allowing regular expression.
     *
     * @param s must not be {@literal null}.
     * @return the {@link Criteria}
     */
    public Criteria regexp(@NonNull final String s) {
        predicates.add(new Predicate(OperationKey.REGEXP, s));
        return this;
    }

    /**
     * Boost {@link Criteria} with given factor.
     *
     * @param boost the boost factor
     * @return the {@link Criteria}
     */
    public Criteria boost(final float boost) {
        if (boost < 0) {
            throw new IllegalArgumentException("Boost must not be negative.");
        }
        this.boost = boost;
        return this;
    }

    /**
     * Creates new {@link Predicate} for {@code null} values.
     *
     * @return the {@link Criteria}
     */
    public Criteria isNull() {
        return between(null, null).mustNot();
    }

    /**
     * Set the {@link Criteria} as required.
     *
     * @return the {@link Criteria}
     */
    public Criteria must() {
        this.setOccur(Occur.MUST);
        return this;
    }

    /**
     * Set the {@link Criteria} as negating.
     *
     * @return the {@link Criteria}
     */
    public Criteria mustNot() {
        this.setOccur(Occur.MUST_NOT);
        return this;
    }

    public Set<Predicate> getPredicates() {
        return Collections.unmodifiableSet(predicates);
    }

    public Field getField() {
        return field;
    }

    /**
     * Boost factor value.
     *
     * @return {@code Float.NaN} if not set
     */
    public float getBoost() {
        return boost;
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    @Override
    protected <T extends Node> T create() {
        return (T) new Criteria();
    }

    public enum OperationKey {
        EQUALS("$equals"), CONTAINS("$contains"), STARTS_WITH("$startsWith"), ENDS_WITH("$endsWith"), EXPRESSION(
            "$expression"), BETWEEN(
            "$between"), NEAR("$near"), WITHIN("$within"), FUZZY("$fuzzy"), SLOPPY("$sloppy"), FUNCTION("$function"), REGEXP("$regexp");

        private final String key;

        OperationKey(final String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public enum Occur {
        MUST {
            @Override
            public String toString() {
                return "+";
            }
        },
        FILTER {
            @Override
            public String toString() {
                return "#";
            }
        },
        SHOULD {
            @Override
            public String toString() {
                return "";
            }
        },
        MUST_NOT {
            @Override
            public String toString() {
                return "-";
            }
        }
    }

    enum Operator {
        OR {
            @Override
            public String toString() {
                return "OR";
            }
        },
        AND {
            @Override
            public String toString() {
                return "AND";
            }
        }
    }

}
