package com.excellence.bankingsystemapi.util;


import java.time.Year;
import java.util.concurrent.ThreadLocalRandom;

public final class CustomerNumberGenerator {

    private CustomerNumberGenerator() {
    }

    public static String generate() {

        int randomNumber =
                ThreadLocalRandom.current()
                        .nextInt(100000, 1_000000);

        return "CUST"
                + Year.now().getValue()
                + randomNumber;
    }

}