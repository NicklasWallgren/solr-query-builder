package dev.nicklasw.solr.query.builder.parser.converter;

import edu.umd.cs.findbugs.annotations.Nullable;

public interface Converter {

    boolean canConvert(final Class<?> sourceType, final Class<?> targetType);

    <T> T convert(@Nullable final Object source, final Class<T> targetType);

}
