package com.example.persistence.mapper;

import com.example.annotation.MapperTest;
import com.example.persistence.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MapperTest
@Sql({"classpath:schema.sql", "classpath:test-data.sql"})
public class CustomerMapperTest {

    @Autowired
    CustomerMapper customerMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Nested
    @DisplayName("selectAll()")
    class SelectAll {
        @Test
        @DisplayName("全件取得できる")
        void success() {
            List<Customer> actual = customerMapper.selectAll();
            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29)), actual.get(0))
            );
        }
    }

    @Nested
    @DisplayName("insert()")
    class Insert {
        @Test
        @DisplayName("1件追加できる")
        void success() {
            int actual = customerMapper.insert(new Customer("天", "山﨑", "tyamasaki@sakura.com", LocalDate.of(2005, 9, 28)));
            assertAll(
                    () -> assertEquals(1, actual),
                    () -> assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "customer", """
                            id = 3
                            AND first_name = '天'
                            AND last_name = '山﨑'
                            AND mail_address = 'tyamasaki@sakura.com'
                            AND birthday = '2005-09-28'
                            """))
            );
        }
    }
}
