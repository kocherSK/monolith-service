package com.osttra.fx.blockstream.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.osttra.fx.blockstream.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SmartTradeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SmartTrade.class);
        SmartTrade smartTrade1 = new SmartTrade();
        smartTrade1.setId("id1");
        SmartTrade smartTrade2 = new SmartTrade();
        smartTrade2.setId(smartTrade1.getId());
        assertThat(smartTrade1).isEqualTo(smartTrade2);
        smartTrade2.setId("id2");
        assertThat(smartTrade1).isNotEqualTo(smartTrade2);
        smartTrade1.setId(null);
        assertThat(smartTrade1).isNotEqualTo(smartTrade2);
    }
}
