package com.example.web.response;

import com.example.persistence.entity.Customer;

import java.time.LocalDate;

public class CustomerResponse {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthday;

    public CustomerResponse(Customer customer) {
        this.id = customer.getId();
        this.firstName = customer.getFirstName();
        this.lastName = customer.getLastName();
        this.email = customer.getEmail();
        this.birthday = customer.getBirthday();
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
}
