package com.osttra.fx.blockstream.web.rest;

import static com.osttra.fx.blockstream.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.osttra.fx.blockstream.IntegrationTest;
import com.osttra.fx.blockstream.domain.SmartTrade;
import com.osttra.fx.blockstream.repository.SmartTradeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SmartTradeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SmartTradeResourceIT {

    private static final String DEFAULT_COUNTER_PARTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTER_PARTY = "BBBBBBBBBB";

    private static final String DEFAULT_TRADING_PARTY = "AAAAAAAAAA";
    private static final String UPDATED_TRADING_PARTY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_BUY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_BUY = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_SELL = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_SELL = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CONTRA_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CONTRA_AMOUNT = new BigDecimal(2);

    private static final LocalDate DEFAULT_VALUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TRANSACTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRANSACTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/smart-trades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SmartTradeRepository smartTradeRepository;

    @Mock
    private SmartTradeRepository smartTradeRepositoryMock;

    @Autowired
    private MockMvc restSmartTradeMockMvc;

    private SmartTrade smartTrade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmartTrade createEntity() {
        SmartTrade smartTrade = new SmartTrade()
            .counterParty(DEFAULT_COUNTER_PARTY)
            .tradingParty(DEFAULT_TRADING_PARTY)
            .currencyBuy(DEFAULT_CURRENCY_BUY)
            .currencySell(DEFAULT_CURRENCY_SELL)
            .rate(DEFAULT_RATE)
            .amount(DEFAULT_AMOUNT)
            .contraAmount(DEFAULT_CONTRA_AMOUNT)
            .valueDate(DEFAULT_VALUE_DATE)
            .transactionId(DEFAULT_TRANSACTION_ID)
            .direction(DEFAULT_DIRECTION);
        return smartTrade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmartTrade createUpdatedEntity() {
        SmartTrade smartTrade = new SmartTrade()
            .counterParty(UPDATED_COUNTER_PARTY)
            .tradingParty(UPDATED_TRADING_PARTY)
            .currencyBuy(UPDATED_CURRENCY_BUY)
            .currencySell(UPDATED_CURRENCY_SELL)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT)
            .contraAmount(UPDATED_CONTRA_AMOUNT)
            .valueDate(UPDATED_VALUE_DATE)
            .transactionId(UPDATED_TRANSACTION_ID)
            .direction(UPDATED_DIRECTION);
        return smartTrade;
    }

    @BeforeEach
    public void initTest() {
        smartTradeRepository.deleteAll();
        smartTrade = createEntity();
    }

    @Test
    void createSmartTrade() throws Exception {
        int databaseSizeBeforeCreate = smartTradeRepository.findAll().size();
        // Create the SmartTrade
        restSmartTradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smartTrade)))
            .andExpect(status().isCreated());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeCreate + 1);
        SmartTrade testSmartTrade = smartTradeList.get(smartTradeList.size() - 1);
        assertThat(testSmartTrade.getCounterParty()).isEqualTo(DEFAULT_COUNTER_PARTY);
        assertThat(testSmartTrade.getTradingParty()).isEqualTo(DEFAULT_TRADING_PARTY);
        assertThat(testSmartTrade.getCurrencyBuy()).isEqualTo(DEFAULT_CURRENCY_BUY);
        assertThat(testSmartTrade.getCurrencySell()).isEqualTo(DEFAULT_CURRENCY_SELL);
        assertThat(testSmartTrade.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testSmartTrade.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testSmartTrade.getContraAmount()).isEqualByComparingTo(DEFAULT_CONTRA_AMOUNT);
        assertThat(testSmartTrade.getValueDate()).isEqualTo(DEFAULT_VALUE_DATE);
        assertThat(testSmartTrade.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSmartTrade.getDirection()).isEqualTo(DEFAULT_DIRECTION);
    }

    @Test
    void createSmartTradeWithExistingId() throws Exception {
        // Create the SmartTrade with an existing ID
        smartTrade.setId("existing_id");

        int databaseSizeBeforeCreate = smartTradeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmartTradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smartTrade)))
            .andExpect(status().isBadRequest());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSmartTrades() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        // Get all the smartTradeList
        restSmartTradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartTrade.getId())))
            .andExpect(jsonPath("$.[*].counterParty").value(hasItem(DEFAULT_COUNTER_PARTY)))
            .andExpect(jsonPath("$.[*].tradingParty").value(hasItem(DEFAULT_TRADING_PARTY)))
            .andExpect(jsonPath("$.[*].currencyBuy").value(hasItem(DEFAULT_CURRENCY_BUY)))
            .andExpect(jsonPath("$.[*].currencySell").value(hasItem(DEFAULT_CURRENCY_SELL)))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].contraAmount").value(hasItem(sameNumber(DEFAULT_CONTRA_AMOUNT))))
            .andExpect(jsonPath("$.[*].valueDate").value(hasItem(DEFAULT_VALUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(DEFAULT_TRANSACTION_ID)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSmartTradesWithEagerRelationshipsIsEnabled() throws Exception {
        when(smartTradeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSmartTradeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(smartTradeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSmartTradesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(smartTradeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSmartTradeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(smartTradeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getSmartTrade() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        // Get the smartTrade
        restSmartTradeMockMvc
            .perform(get(ENTITY_API_URL_ID, smartTrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smartTrade.getId()))
            .andExpect(jsonPath("$.counterParty").value(DEFAULT_COUNTER_PARTY))
            .andExpect(jsonPath("$.tradingParty").value(DEFAULT_TRADING_PARTY))
            .andExpect(jsonPath("$.currencyBuy").value(DEFAULT_CURRENCY_BUY))
            .andExpect(jsonPath("$.currencySell").value(DEFAULT_CURRENCY_SELL))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.contraAmount").value(sameNumber(DEFAULT_CONTRA_AMOUNT)))
            .andExpect(jsonPath("$.valueDate").value(DEFAULT_VALUE_DATE.toString()))
            .andExpect(jsonPath("$.transactionId").value(DEFAULT_TRANSACTION_ID))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION));
    }

    @Test
    void getNonExistingSmartTrade() throws Exception {
        // Get the smartTrade
        restSmartTradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSmartTrade() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();

        // Update the smartTrade
        SmartTrade updatedSmartTrade = smartTradeRepository.findById(smartTrade.getId()).get();
        updatedSmartTrade
            .counterParty(UPDATED_COUNTER_PARTY)
            .tradingParty(UPDATED_TRADING_PARTY)
            .currencyBuy(UPDATED_CURRENCY_BUY)
            .currencySell(UPDATED_CURRENCY_SELL)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT)
            .contraAmount(UPDATED_CONTRA_AMOUNT)
            .valueDate(UPDATED_VALUE_DATE)
            .transactionId(UPDATED_TRANSACTION_ID)
            .direction(UPDATED_DIRECTION);

        restSmartTradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSmartTrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSmartTrade))
            )
            .andExpect(status().isOk());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
        SmartTrade testSmartTrade = smartTradeList.get(smartTradeList.size() - 1);
        assertThat(testSmartTrade.getCounterParty()).isEqualTo(UPDATED_COUNTER_PARTY);
        assertThat(testSmartTrade.getTradingParty()).isEqualTo(UPDATED_TRADING_PARTY);
        assertThat(testSmartTrade.getCurrencyBuy()).isEqualTo(UPDATED_CURRENCY_BUY);
        assertThat(testSmartTrade.getCurrencySell()).isEqualTo(UPDATED_CURRENCY_SELL);
        assertThat(testSmartTrade.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testSmartTrade.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSmartTrade.getContraAmount()).isEqualByComparingTo(UPDATED_CONTRA_AMOUNT);
        assertThat(testSmartTrade.getValueDate()).isEqualTo(UPDATED_VALUE_DATE);
        assertThat(testSmartTrade.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSmartTrade.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void putNonExistingSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smartTrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smartTrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(smartTrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(smartTrade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSmartTradeWithPatch() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();

        // Update the smartTrade using partial update
        SmartTrade partialUpdatedSmartTrade = new SmartTrade();
        partialUpdatedSmartTrade.setId(smartTrade.getId());

        partialUpdatedSmartTrade.currencySell(UPDATED_CURRENCY_SELL).rate(UPDATED_RATE).amount(UPDATED_AMOUNT).direction(UPDATED_DIRECTION);

        restSmartTradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmartTrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmartTrade))
            )
            .andExpect(status().isOk());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
        SmartTrade testSmartTrade = smartTradeList.get(smartTradeList.size() - 1);
        assertThat(testSmartTrade.getCounterParty()).isEqualTo(DEFAULT_COUNTER_PARTY);
        assertThat(testSmartTrade.getTradingParty()).isEqualTo(DEFAULT_TRADING_PARTY);
        assertThat(testSmartTrade.getCurrencyBuy()).isEqualTo(DEFAULT_CURRENCY_BUY);
        assertThat(testSmartTrade.getCurrencySell()).isEqualTo(UPDATED_CURRENCY_SELL);
        assertThat(testSmartTrade.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testSmartTrade.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSmartTrade.getContraAmount()).isEqualByComparingTo(DEFAULT_CONTRA_AMOUNT);
        assertThat(testSmartTrade.getValueDate()).isEqualTo(DEFAULT_VALUE_DATE);
        assertThat(testSmartTrade.getTransactionId()).isEqualTo(DEFAULT_TRANSACTION_ID);
        assertThat(testSmartTrade.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void fullUpdateSmartTradeWithPatch() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();

        // Update the smartTrade using partial update
        SmartTrade partialUpdatedSmartTrade = new SmartTrade();
        partialUpdatedSmartTrade.setId(smartTrade.getId());

        partialUpdatedSmartTrade
            .counterParty(UPDATED_COUNTER_PARTY)
            .tradingParty(UPDATED_TRADING_PARTY)
            .currencyBuy(UPDATED_CURRENCY_BUY)
            .currencySell(UPDATED_CURRENCY_SELL)
            .rate(UPDATED_RATE)
            .amount(UPDATED_AMOUNT)
            .contraAmount(UPDATED_CONTRA_AMOUNT)
            .valueDate(UPDATED_VALUE_DATE)
            .transactionId(UPDATED_TRANSACTION_ID)
            .direction(UPDATED_DIRECTION);

        restSmartTradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmartTrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSmartTrade))
            )
            .andExpect(status().isOk());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
        SmartTrade testSmartTrade = smartTradeList.get(smartTradeList.size() - 1);
        assertThat(testSmartTrade.getCounterParty()).isEqualTo(UPDATED_COUNTER_PARTY);
        assertThat(testSmartTrade.getTradingParty()).isEqualTo(UPDATED_TRADING_PARTY);
        assertThat(testSmartTrade.getCurrencyBuy()).isEqualTo(UPDATED_CURRENCY_BUY);
        assertThat(testSmartTrade.getCurrencySell()).isEqualTo(UPDATED_CURRENCY_SELL);
        assertThat(testSmartTrade.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testSmartTrade.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testSmartTrade.getContraAmount()).isEqualByComparingTo(UPDATED_CONTRA_AMOUNT);
        assertThat(testSmartTrade.getValueDate()).isEqualTo(UPDATED_VALUE_DATE);
        assertThat(testSmartTrade.getTransactionId()).isEqualTo(UPDATED_TRANSACTION_ID);
        assertThat(testSmartTrade.getDirection()).isEqualTo(UPDATED_DIRECTION);
    }

    @Test
    void patchNonExistingSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smartTrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smartTrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(smartTrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSmartTrade() throws Exception {
        int databaseSizeBeforeUpdate = smartTradeRepository.findAll().size();
        smartTrade.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartTradeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(smartTrade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmartTrade in the database
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSmartTrade() throws Exception {
        // Initialize the database
        smartTradeRepository.save(smartTrade);

        int databaseSizeBeforeDelete = smartTradeRepository.findAll().size();

        // Delete the smartTrade
        restSmartTradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, smartTrade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SmartTrade> smartTradeList = smartTradeRepository.findAll();
        assertThat(smartTradeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
