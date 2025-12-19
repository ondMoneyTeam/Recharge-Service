package com.onemoney.recgardecardservice.domain;

import static com.onemoney.recgardecardservice.domain.CardTestSamples.*;
import static com.onemoney.recgardecardservice.domain.RechargeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.onemoney.recgardecardservice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RechargeTest {



    @Test
    void cardTest() {
        Recharge recharge = getRechargeRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        recharge.setCard(cardBack);
        assertThat(recharge.getCard()).isEqualTo(cardBack);

        recharge.card(null);
        assertThat(recharge.getCard()).isNull();
    }
}
