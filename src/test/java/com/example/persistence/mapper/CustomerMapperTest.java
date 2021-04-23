package com.example.persistence.mapper;

import com.example.persistence.entity.Customer;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerMapperTest {

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void findAllメソッドで5件取得できる() {
        List<Customer> customerList = customerMapper.findAll();
        assertEquals(5, customerList.size());
    }

    @Test
    void saveメソッドで1件追加できる() {
        Customer newCustomer = new Customer("天", "山﨑", "tyamasaki@sakura.com", LocalDate.of(2005, 9, 28));
        customerMapper.save(newCustomer);
        assertEquals(6, JdbcTestUtils.countRowsInTable(jdbcTemplate, "customer"));
    }
}
