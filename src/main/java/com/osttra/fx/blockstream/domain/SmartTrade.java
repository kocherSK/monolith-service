package com.osttra.fx.blockstream.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A SmartTrade.
 */
@Document(collection = "smart_trade")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SmartTrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("counter_party")
    private String counterParty;

    @Field("trading_party")
    private String tradingParty;

    @Field("currency_buy")
    private String currencyBuy;

    @Field("currency_sell")
    private String currencySell;

    @Field("rate")
    private Double rate;

    @Field("amount")
    private BigDecimal amount;

    @Field("contra_amount")
    private BigDecimal contraAmount;

    @Field("value_date")
    private LocalDate valueDate;

    @Field("transaction_id")
    private String transactionId;

    @Field("direction")
    private String direction;

    @DBRef
    @Field("customer")
    @JsonIgnoreProperties(value = { "userId" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public SmartTrade id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCounterParty() {
        return this.counterParty;
    }

    public SmartTrade counterParty(String counterParty) {
        this.setCounterParty(counterParty);
        return this;
    }

    public void setCounterParty(String counterParty) {
        this.counterParty = counterParty;
    }

    public String getTradingParty() {
        return this.tradingParty;
    }

    public SmartTrade tradingParty(String tradingParty) {
        this.setTradingParty(tradingParty);
        return this;
    }

    public void setTradingParty(String tradingParty) {
        this.tradingParty = tradingParty;
    }

    public String getCurrencyBuy() {
        return this.currencyBuy;
    }

    public SmartTrade currencyBuy(String currencyBuy) {
        this.setCurrencyBuy(currencyBuy);
        return this;
    }

    public void setCurrencyBuy(String currencyBuy) {
        this.currencyBuy = currencyBuy;
    }

    public String getCurrencySell() {
        return this.currencySell;
    }

    public SmartTrade currencySell(String currencySell) {
        this.setCurrencySell(currencySell);
        return this;
    }

    public void setCurrencySell(String currencySell) {
        this.currencySell = currencySell;
    }

    public Double getRate() {
        return this.rate;
    }

    public SmartTrade rate(Double rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public SmartTrade amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getContraAmount() {
        return this.contraAmount;
    }

    public SmartTrade contraAmount(BigDecimal contraAmount) {
        this.setContraAmount(contraAmount);
        return this;
    }

    public void setContraAmount(BigDecimal contraAmount) {
        this.contraAmount = contraAmount;
    }

    public LocalDate getValueDate() {
        return this.valueDate;
    }

    public SmartTrade valueDate(LocalDate valueDate) {
        this.setValueDate(valueDate);
        return this;
    }

    public void setValueDate(LocalDate valueDate) {
        this.valueDate = valueDate;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public SmartTrade transactionId(String transactionId) {
        this.setTransactionId(transactionId);
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDirection() {
        return this.direction;
    }

    public SmartTrade direction(String direction) {
        this.setDirection(direction);
        return this;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public SmartTrade customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmartTrade)) {
            return false;
        }
        return id != null && id.equals(((SmartTrade) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmartTrade{" +
            "id=" + getId() +
            ", counterParty='" + getCounterParty() + "'" +
            ", tradingParty='" + getTradingParty() + "'" +
            ", currencyBuy='" + getCurrencyBuy() + "'" +
            ", currencySell='" + getCurrencySell() + "'" +
            ", rate=" + getRate() +
            ", amount=" + getAmount() +
            ", contraAmount=" + getContraAmount() +
            ", valueDate='" + getValueDate() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", direction='" + getDirection() + "'" +
            "}";
    }
}
