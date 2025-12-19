package com.onemoney.recgardecardservice.service.mapper;

import static com.onemoney.recgardecardservice.domain.RechargeAsserts.*;
import static com.onemoney.recgardecardservice.domain.RechargeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RechargeMapperTest {

    private RechargeMapper rechargeMapper;

    @BeforeEach
    void setUp() {
        rechargeMapper = new RechargeMapperImpl();
    }


}
