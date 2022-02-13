package dev.nicklasw.query.builder.parser.converter;

import java.util.ArrayList;
import java.util.List;

public class ConverterProcessor {

    private final List<Converter> converters = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public <T> T convert(final Object object, final Class<T> targetType) {
        return converters.stream().filter(it -> it.canConvert(object.getClass(), targetType))
            .map(it -> it.convert(object, targetType))
            .findFirst()
            .orElse((T) object);
    }

}
