package com.osttra.fx.blockstream.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.osttra.fx.blockstream.IntegrationTest;
import com.osttra.fx.blockstream.domain.Currencies;
import com.osttra.fx.blockstream.repository.CurrenciesRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link CurrenciesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CurrenciesResourceIT {

    private static final String DEFAULT_CURRENCY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/currencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CurrenciesRepository currenciesRepository;

    @Autowired
    private MockMvc restCurrenciesMockMvc;

    private Currencies currencies;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currencies createEntity() {
        Currencies currencies = new Currencies().currencyName(DEFAULT_CURRENCY_NAME).currencyCode(DEFAULT_CURRENCY_CODE);
        return currencies;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Currencies createUpdatedEntity() {
        Currencies currencies = new Currencies().currencyName(UPDATED_CURRENCY_NAME).currencyCode(UPDATED_CURRENCY_CODE);
        return currencies;
    }

    @BeforeEach
    public void initTest() {
        currenciesRepository.deleteAll();
        currencies = createEntity();
    }

    @Test
    void createCurrencies() throws Exception {
        int databaseSizeBeforeCreate = currenciesRepository.findAll().size();
        // Create the Currencies
        restCurrenciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencies)))
            .andExpect(status().isCreated());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeCreate + 1);
        Currencies testCurrencies = currenciesList.get(currenciesList.size() - 1);
        assertThat(testCurrencies.getCurrencyName()).isEqualTo(DEFAULT_CURRENCY_NAME);
        assertThat(testCurrencies.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
    }

    @Test
    void createCurrenciesWithExistingId() throws Exception {
        // Create the Currencies with an existing ID
        currencies.setId("existing_id");

        int databaseSizeBeforeCreate = currenciesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrenciesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencies)))
            .andExpect(status().isBadRequest());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCurrencies() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        // Get all the currenciesList
        restCurrenciesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currencies.getId())))
            .andExpect(jsonPath("$.[*].currencyName").value(hasItem(DEFAULT_CURRENCY_NAME)))
            .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)));
    }

    @Test
    void getCurrencies() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        // Get the currencies
        restCurrenciesMockMvc
            .perform(get(ENTITY_API_URL_ID, currencies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(currencies.getId()))
            .andExpect(jsonPath("$.currencyName").value(DEFAULT_CURRENCY_NAME))
            .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE));
    }

    @Test
    void getNonExistingCurrencies() throws Exception {
        // Get the currencies
        restCurrenciesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCurrencies() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();

        // Update the currencies
        Currencies updatedCurrencies = currenciesRepository.findById(currencies.getId()).get();
        updatedCurrencies.currencyName(UPDATED_CURRENCY_NAME).currencyCode(UPDATED_CURRENCY_CODE);

        restCurrenciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurrencies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCurrencies))
            )
            .andExpect(status().isOk());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
        Currencies testCurrencies = currenciesList.get(currenciesList.size() - 1);
        assertThat(testCurrencies.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testCurrencies.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
    }

    @Test
    void putNonExistingCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, currencies.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(currencies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(currencies)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCurrenciesWithPatch() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();

        // Update the currencies using partial update
        Currencies partialUpdatedCurrencies = new Currencies();
        partialUpdatedCurrencies.setId(currencies.getId());

        partialUpdatedCurrencies.currencyCode(UPDATED_CURRENCY_CODE);

        restCurrenciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencies))
            )
            .andExpect(status().isOk());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
        Currencies testCurrencies = currenciesList.get(currenciesList.size() - 1);
        assertThat(testCurrencies.getCurrencyName()).isEqualTo(DEFAULT_CURRENCY_NAME);
        assertThat(testCurrencies.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
    }

    @Test
    void fullUpdateCurrenciesWithPatch() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();

        // Update the currencies using partial update
        Currencies partialUpdatedCurrencies = new Currencies();
        partialUpdatedCurrencies.setId(currencies.getId());

        partialUpdatedCurrencies.currencyName(UPDATED_CURRENCY_NAME).currencyCode(UPDATED_CURRENCY_CODE);

        restCurrenciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurrencies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurrencies))
            )
            .andExpect(status().isOk());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
        Currencies testCurrencies = currenciesList.get(currenciesList.size() - 1);
        assertThat(testCurrencies.getCurrencyName()).isEqualTo(UPDATED_CURRENCY_NAME);
        assertThat(testCurrencies.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
    }

    @Test
    void patchNonExistingCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, currencies.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(currencies))
            )
            .andExpect(status().isBadRequest());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCurrencies() throws Exception {
        int databaseSizeBeforeUpdate = currenciesRepository.findAll().size();
        currencies.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurrenciesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(currencies))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Currencies in the database
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCurrencies() throws Exception {
        // Initialize the database
        currenciesRepository.save(currencies);

        int databaseSizeBeforeDelete = currenciesRepository.findAll().size();

        // Delete the currencies
        restCurrenciesMockMvc
            .perform(delete(ENTITY_API_URL_ID, currencies.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Currencies> currenciesList = currenciesRepository.findAll();
        assertThat(currenciesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
