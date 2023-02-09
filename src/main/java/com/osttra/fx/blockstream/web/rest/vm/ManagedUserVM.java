package com.osttra.fx.blockstream.web.rest.vm;

import com.osttra.fx.blockstream.service.dto.AdminUserDTO;
import javax.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerLegalEntity() {
        return customerLegalEntity;
    }

    public String getCustomerPassword() {
        return customerPassword;
    }

    public String getCustomerHashCode() {
        return customerHashCode;
    }

    @Field("customer_name")
    private String customerName;

    @Field("customer_legal_entity")
    private String customerLegalEntity;

    @Field("customer_password")
    private String customerPassword;

    @Field("customer_hash_code")
    private String customerHashCode;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
