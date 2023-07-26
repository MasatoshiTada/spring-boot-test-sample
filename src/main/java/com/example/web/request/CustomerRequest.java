package com.example.web.request;

import com.example.persistence.entity.Customer;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CustomerRequest {

    @NotBlank
    @Length(min = 1, max = 32)
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 32)
    private String lastName;

    @NotBlank
    @Length(min = 1, max = 128)
    @Email
    private String email;

    @NotNull
    private LocalDate birthday;

    @JsonCreator
    public CustomerRequest(String firstName, String lastName, String email, LocalDate birthday) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthday = birthday;
    }

    public Customer convertToEntity() {
        return new Customer(firstName, lastName, email, birthday);
    }
}
