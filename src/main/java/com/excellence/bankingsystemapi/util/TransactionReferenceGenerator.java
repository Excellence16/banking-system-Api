





package com.excellence.bankingsystemapi.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class TransactionReferenceGenerator {

    private TransactionReferenceGenerator() {
    }


    public static String generate() {

        String timestamp =
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));


        String randomPart =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();


        return "TXN-" + timestamp + "-" + randomPart;
    }
}
