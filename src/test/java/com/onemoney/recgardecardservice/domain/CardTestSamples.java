package com.onemoney.recgardecardservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CardTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Card getCardSample1() {
        return new Card().id(1L).accountId(1L).cardNumber("cardNumber1").cardType("cardType1");
    }

    public static Card getCardSample2() {
        return new Card().id(2L).accountId(2L).cardNumber("cardNumber2").cardType("cardType2");
    }

    public static Card getCardRandomSampleGenerator() {
        return new Card()
            .id(longCount.incrementAndGet())
            .accountId(longCount.incrementAndGet())
            .cardNumber(UUID.randomUUID().toString())
            .cardType(UUID.randomUUID().toString());
    }
}
