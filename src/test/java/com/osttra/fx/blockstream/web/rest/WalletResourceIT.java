package com.osttra.fx.blockstream.web.rest;

import static com.osttra.fx.blockstream.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.osttra.fx.blockstream.IntegrationTest;
import com.osttra.fx.blockstream.domain.Wallet;
import com.osttra.fx.blockstream.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link WalletResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WalletResourceIT {

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/wallets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private WalletRepository walletRepository;

    @Mock
    private WalletRepository walletRepositoryMock;

    @Autowired
    private MockMvc restWalletMockMvc;

    private Wallet wallet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createEntity() {
        Wallet wallet = new Wallet().currencyCode(DEFAULT_CURRENCY_CODE).amount(DEFAULT_AMOUNT);
        return wallet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wallet createUpdatedEntity() {
        Wallet wallet = new Wallet().currencyCode(UPDATED_CURRENCY_CODE).amount(UPDATED_AMOUNT);
        return wallet;
    }

    @BeforeEach
    public void initTest() {
        walletRepository.deleteAll();
        wallet = createEntity();
    }

    @Test
    void createWallet() throws Exception {
        int databaseSizeBeforeCreate = walletRepository.findAll().size();
        // Create the Wallet
        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet)))
            .andExpect(status().isCreated());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate + 1);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testWallet.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
    }

    @Test
    void createWalletWithExistingId() throws Exception {
        // Create the Wallet with an existing ID
        wallet.setId("existing_id");

        int databaseSizeBeforeCreate = walletRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet)))
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllWallets() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        // Get all the walletList
        restWalletMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wallet.getId())))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletsWithEagerRelationshipsIsEnabled() throws Exception {
        when(walletRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(walletRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWalletsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(walletRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWalletMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(walletRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getWallet() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        // Get the wallet
        restWalletMockMvc
            .perform(get(ENTITY_API_URL_ID, wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wallet.getId()))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)));
    }

    @Test
    void getNonExistingWallet() throws Exception {
        // Get the wallet
        restWalletMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingWallet() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet
        Wallet updatedWallet = walletRepository.findById(wallet.getId()).get();
        updatedWallet.currencyCode(UPDATED_CURRENCY_CODE).amount(UPDATED_AMOUNT);

        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testWallet.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void putNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wallet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.currencyCode(UPDATED_CURRENCY_CODE).amount(UPDATED_AMOUNT);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testWallet.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void fullUpdateWalletWithPatch() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        int databaseSizeBeforeUpdate = walletRepository.findAll().size();

        // Update the wallet using partial update
        Wallet partialUpdatedWallet = new Wallet();
        partialUpdatedWallet.setId(wallet.getId());

        partialUpdatedWallet.currencyCode(UPDATED_CURRENCY_CODE).amount(UPDATED_AMOUNT);

        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWallet))
            )
            .andExpect(status().isOk());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
        Wallet testWallet = walletList.get(walletList.size() - 1);
        assertThat(testWallet.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testWallet.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
    }

    @Test
    void patchNonExistingWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wallet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wallet))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamWallet() throws Exception {
        int databaseSizeBeforeUpdate = walletRepository.findAll().size();
        wallet.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWalletMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wallet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wallet in the database
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteWallet() throws Exception {
        // Initialize the database
        walletRepository.save(wallet);

        int databaseSizeBeforeDelete = walletRepository.findAll().size();

        // Delete the wallet
        restWalletMockMvc
            .perform(delete(ENTITY_API_URL_ID, wallet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Wallet> walletList = walletRepository.findAll();
        assertThat(walletList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
