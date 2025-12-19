package com.onemoney.recgardecardservice.domain;

import static com.onemoney.recgardecardservice.domain.CardTestSamples.*;
import static com.onemoney.recgardecardservice.domain.RechargeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.onemoney.recgardecardservice.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = getCardSample1();
        Card card2 = new Card();
        assertThat(card1).isNotEqualTo(card2);

        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);

        card2 = getCardSample2();
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void rechargeTest() {
        Card card = getCardRandomSampleGenerator();
        Recharge rechargeBack = getRechargeRandomSampleGenerator();

        card.addRecharge(rechargeBack);
        assertThat(card.getRecharges()).containsOnly(rechargeBack);
        assertThat(rechargeBack.getCard()).isEqualTo(card);

        card.removeRecharge(rechargeBack);
        assertThat(card.getRecharges()).doesNotContain(rechargeBack);
        assertThat(rechargeBack.getCard()).isNull();

        card.recharges(new HashSet<>(Set.of(rechargeBack)));
        assertThat(card.getRecharges()).containsOnly(rechargeBack);
        assertThat(rechargeBack.getCard()).isEqualTo(card);

        card.setRecharges(new HashSet<>());
        assertThat(card.getRecharges()).doesNotContain(rechargeBack);
        assertThat(rechargeBack.getCard()).isNull();
    }
}
