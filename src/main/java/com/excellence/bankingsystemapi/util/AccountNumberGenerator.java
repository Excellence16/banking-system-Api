
package com.excellence.bankingsystemapi.util;




import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AccountNumberGenerator {

    private static final int ACCOUNT_NUMBER_LENGTH = 10;

    public String generate() {

        long number = ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L);

        return String.valueOf(number);
    }
}