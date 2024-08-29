package com.example.service;

import com.example.persistence.entity.Customer;
import com.example.persistence.mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    CustomerService customerService;

    CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        // CustomerMapperのモックを作成
        customerMapper = mock(CustomerMapper.class);
        // CustomerMapperのモックを利用してCustomerServiceImplインスタンスを作成
        customerService = new CustomerService(customerMapper);
    }

    @Nested
    @DisplayName("findAll()")
    class FindAllTest {
        @Test
        @DisplayName("全件取得できる")
        void success () {
            // CustomerMapperのfindAll()に仮の戻り値を設定
            when(customerMapper.selectAll()).thenReturn(List.of(
                    new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29)),
                    new Customer(2, "久美", "佐々木", "ksasaki@hinata.com", LocalDate.of(1996, 1, 22))
            ));
            // テスト対象のメソッドを実行
            List<Customer> actual = customerService.findAll();
            // テスト対象の戻り値を検証
            assertAll(
                    () -> assertEquals(2, actual.size()),
                    () -> assertEquals(new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29)), actual.get(0))
            );
        }
    }

    @Nested
    @DisplayName("save()")
    class Save {
        @Test
        void success() {
            // テスト対象メソッドに与える引数
            Customer newCustomer = new Customer("天", "山﨑", "tyamasaki@sakura.com", LocalDate.of(2005, 9, 28));
            // CustomerMapperのsave()が実行されたら1を返すように設定
            when(customerMapper.insert(any())).thenReturn(1);
            // テスト対象のメソッドを実行
            customerService.save(newCustomer);
            // CustomerMapperのsave()が1回呼ばれていることをチェック
            verify(customerMapper, times(1)).insert(newCustomer);
        }
    }
}
