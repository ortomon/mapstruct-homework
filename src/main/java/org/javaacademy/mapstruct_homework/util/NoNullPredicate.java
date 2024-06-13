package org.javaacademy.mapstruct_homework.util;

import java.util.function.Predicate;

public class NoNullPredicate implements Predicate<String> {
    @Override
    public boolean test(String value) {
        return value != null && !value.isEmpty();
    }
}
