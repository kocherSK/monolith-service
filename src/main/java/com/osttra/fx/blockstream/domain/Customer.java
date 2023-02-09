package com.osttra.fx.blockstream.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Customer.
 */
@Document(collection = "customer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("customer_name")
    private String customerName;

    @Field("customer_legal_entity")
    private String customerLegalEntity;

    @Field("customer_password")
    private String customerPassword;

    @Field("customer_hash_code")
    private String customerHashCode;

    @DBRef
    @Field("userId")
    private User userId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Customer id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Customer customerName(String customerName) {
        this.setCustomerName(customerName);
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLegalEntity() {
        return this.customerLegalEntity;
    }

    public Customer customerLegalEntity(String customerLegalEntity) {
        this.setCustomerLegalEntity(customerLegalEntity);
        return this;
    }

    public void setCustomerLegalEntity(String customerLegalEntity) {
        this.customerLegalEntity = customerLegalEntity;
    }

    public String getCustomerPassword() {
        return this.customerPassword;
    }

    public Customer customerPassword(String customerPassword) {
        this.setCustomerPassword(customerPassword);
        return this;
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }

    public String getCustomerHashCode() {
        return this.customerHashCode;
    }

    public Customer customerHashCode(String customerHashCode) {
        this.setCustomerHashCode(customerHashCode);
        return this;
    }

    public void setCustomerHashCode(String customerHashCode) {
        this.customerHashCode = customerHashCode;
    }

    public User getUserId() {
        return this.userId;
    }

    public void setUserId(User user) {
        this.userId = user;
    }

    public Customer userId(User user) {
        this.setUserId(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", customerLegalEntity='" + getCustomerLegalEntity() + "'" +
            ", customerPassword='" + getCustomerPassword() + "'" +
            ", customerHashCode='" + getCustomerHashCode() + "'" +
            "}";
    }
}
