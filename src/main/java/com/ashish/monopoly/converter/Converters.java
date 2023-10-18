package com.ashish.monopoly.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Converters {

    private Converters() {
        // Avoid Initialization from Outside.
    }

    public static <SOURCE, TARGET> Set<TARGET> convertAll
            (final Collection<? extends SOURCE> sourceCollection,
             final Converter<SOURCE, TARGET> converter) {

        Assert.notNull(converter, "Converter can't be null.");
        if (sourceCollection == null || sourceCollection.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<TARGET> result = new LinkedHashSet<>(sourceCollection.size());
        for (final SOURCE source : sourceCollection) {
            result.add(converter.convert(source));
        }
        return result;
    }
}
