package com.example.service.impl;

import com.example.persistence.entity.Customer;
import com.example.persistence.mapper.CustomerMapper;
import com.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    CustomerService customerService;
    CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        // CustomerMapperのモックを作成
        customerMapper = mock(CustomerMapper.class);
        // CustomerMapperのモックを利用してCustomerServiceImplインスタンスを作成
        customerService = new CustomerServiceImpl(customerMapper);
    }

    @Test
    void findAllメソッドで5件取得できる() {
        // CustomerMapperのfindAll()に仮の戻り値を設定
        when(customerMapper.findAll()).thenReturn(List.of(
                new Customer(1, "友香", "菅井", "ysugai@sakura.com", LocalDate.of(1995, 11, 29)),
                new Customer(2, "久美", "佐々木", "ksasaki@hinata.com", LocalDate.of(1996, 1, 22)),
                new Customer(3, "美玖", "金村", "mkanemura@hinata.com", LocalDate.of(2002, 9, 10))
        ));
        // テスト対象のメソッドを実行
        List<Customer> customerList = customerService.findAll();
        // テスト対象の戻り値を検証
        assertEquals(3, customerList.size());
        // CustomerMapperのfindAll()が1回呼ばれていることをチェック
        verify(customerMapper, times(1)).findAll();
    }

    @Test
    void saveメソッドで1件追加できる() {
        // テスト対象メソッドに与える引数
        Customer newCustomer = new Customer("天", "山﨑", "tyamasaki@sakura.com", LocalDate.of(2005, 9, 28));
        // CustomerMapperのsave()が実行されたら何も行わないよう設定
        doNothing().when(customerMapper).save(newCustomer);
        // テスト対象のメソッドを実行
        customerService.save(newCustomer);
        // CustomerMapperのsave()が1回呼ばれていることをチェック
        verify(customerMapper, times(1)).save(newCustomer);
    }
}
