package com.example.persistence.mapper;

import com.example.persistence.entity.Customer;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
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
    @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
    void findAllメソッドで5件取得できる() {
        List<Customer> customerList = customerMapper.findAll();
        assertEquals(2, customerList.size());
    }

    @Test
    @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
    void saveメソッドで1件追加できる() {
        Customer newCustomer = new Customer("天", "山﨑", "tyamasaki@sakura.com", LocalDate.of(2005, 9, 28));
        customerMapper.save(newCustomer);
        assertEquals(3, JdbcTestUtils.countRowsInTable(jdbcTemplate, "customer"));
    }
}
