package com.osttra.fx.blockstream.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.osttra.fx.blockstream.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CurrenciesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Currencies.class);
        Currencies currencies1 = new Currencies();
        currencies1.setId("id1");
        Currencies currencies2 = new Currencies();
        currencies2.setId(currencies1.getId());
        assertThat(currencies1).isEqualTo(currencies2);
        currencies2.setId("id2");
        assertThat(currencies1).isNotEqualTo(currencies2);
        currencies1.setId(null);
        assertThat(currencies1).isNotEqualTo(currencies2);
    }
}
