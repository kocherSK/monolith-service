package com.osttra.fx.blockstream.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Currencies.
 */
@Document(collection = "currencies")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Currencies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("currency_name")
    private String currencyName;

    @Field("currency_code")
    private String currencyCode;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Currencies id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public Currencies currencyName(String currencyName) {
        this.setCurrencyName(currencyName);
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public Currencies currencyCode(String currencyCode) {
        this.setCurrencyCode(currencyCode);
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Currencies)) {
            return false;
        }
        return id != null && id.equals(((Currencies) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Currencies{" +
            "id=" + getId() +
            ", currencyName='" + getCurrencyName() + "'" +
            ", currencyCode='" + getCurrencyCode() + "'" +
            "}";
    }
}
