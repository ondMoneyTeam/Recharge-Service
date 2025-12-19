package com.onemoney.recgardecardservice.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RechargeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));



    public static Recharge getRechargeRandomSampleGenerator() {
        return new Recharge()
            .id(longCount.incrementAndGet())
            .accoundId(longCount.incrementAndGet())
            .status(UUID.randomUUID().toString());
    }
}
