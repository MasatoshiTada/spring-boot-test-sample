package com.example.persistence.entity;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 顧客を表すエンティティクラスです。
 */
// MyBatisのuseGeneratedKeys属性を使用するために、あえてrecordではなくclassを使用しています
public class Customer {

    private Integer id;

    private String firstName;

    private String lastName;

    private String mailAddress;

    private LocalDate birthday;

    public Customer() {
    }

    public Customer(Integer id, String firstName, String lastName, String mailAddress, LocalDate birthday) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.birthday = birthday;
    }

    public Customer(String firstName, String lastName, String mailAddress, LocalDate birthday) {
        this(null, firstName, lastName, mailAddress, birthday);
    }

    public Integer id() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String mailAddress() {
        return mailAddress;
    }

    public LocalDate birthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.id) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(mailAddress, customer.mailAddress) && Objects.equals(birthday, customer.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, mailAddress, birthday);
    }
}
