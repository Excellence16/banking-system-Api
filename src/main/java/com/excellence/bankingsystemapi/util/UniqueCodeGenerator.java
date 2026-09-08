package com.excellence.bankingsystemapi.util;

import java.util.function.Predicate;
import java.util.function.Supplier;


public final class UniqueCodeGenerator {

    private UniqueCodeGenerator() {
    }

    public static String generateUnique(Supplier<String> generator, Predicate<String> existsCheck) {
        String candidate;
        do {
            candidate = generator.get();
        } while (existsCheck.test(candidate));
        return candidate;
    }
}