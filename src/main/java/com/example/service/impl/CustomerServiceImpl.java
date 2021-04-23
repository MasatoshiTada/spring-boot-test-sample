package com.example.service.impl;

import com.example.persistence.entity.Customer;
import com.example.persistence.mapper.CustomerMapper;
import com.example.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerMapper customerMapper) {
        this.customerMapper = customerMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<Customer> findAll() {
        return customerMapper.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public void save(Customer customer) {
        customerMapper.save(customer);
    }
}
