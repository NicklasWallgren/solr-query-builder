package dev.nicklasw.query.builder.fields;

public interface Field {

    /**
     * Returns the name of the solr field.
     *
     * @return the field name
     */
    String getName();

    /**
     * Create a {@link Field} with given name.
     *
     * @param name must not be {@literal null}.
     * @return new instance of {@link Field}.
     */
    static Field of(String name) {
        return new SimpleField(name);
    }

}
