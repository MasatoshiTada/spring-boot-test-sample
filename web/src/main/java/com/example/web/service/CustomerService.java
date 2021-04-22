package com.example.web.service;

import com.example.web.persistence.entity.Customer;

import java.util.List;

public interface CustomerService {

    /**
     * 顧客を全件検索する
     */
    List<Customer> findAll();

    /**
     * 1件の顧客をDBに追加する
     * @param customer 追加する顧客
     */
    void save(Customer customer);
}
