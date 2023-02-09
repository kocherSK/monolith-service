package com.osttra.fx.blockstream.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.osttra.fx.blockstream.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WalletTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Wallet.class);
        Wallet wallet1 = new Wallet();
        wallet1.setId("id1");
        Wallet wallet2 = new Wallet();
        wallet2.setId(wallet1.getId());
        assertThat(wallet1).isEqualTo(wallet2);
        wallet2.setId("id2");
        assertThat(wallet1).isNotEqualTo(wallet2);
        wallet1.setId(null);
        assertThat(wallet1).isNotEqualTo(wallet2);
    }
}
