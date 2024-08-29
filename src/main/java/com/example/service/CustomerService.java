package com.example.service;

import com.example.persistence.entity.Customer;
import com.example.persistence.mapper.CustomerMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 顧客情報を操作するサービスクラスです。
 */
@Service
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerService(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    /**
     * 顧客情報を全件取得します。
     * @return 顧客情報のリスト
     */
    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return customerMapper.selectAll();
    }

    /**
     * 顧客情報を登録します。
     * @param customer 顧客情報
     */
    @Transactional(readOnly = false)
    public void save(Customer customer) {
        customerMapper.insert(customer);
    }
}
