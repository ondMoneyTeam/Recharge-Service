package com.onemoney.recgardecardservice;

import com.onemoney.recgardecardservice.config.AsyncSyncConfiguration;
import com.onemoney.recgardecardservice.config.EmbeddedKafka;
import com.onemoney.recgardecardservice.config.EmbeddedSQL;
import com.onemoney.recgardecardservice.config.JacksonConfiguration;
import com.onemoney.recgardecardservice.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        OneMoneyRechargeCardServiceApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class, TestSecurityConfiguration.class,
    }
)
@EmbeddedSQL
@EmbeddedKafka
public @interface IntegrationTest {
}
