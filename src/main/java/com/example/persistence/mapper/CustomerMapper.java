package com.example.persistence.mapper;

import com.example.persistence.entity.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 顧客情報を操作するマッパーインターフェースです。
 */
@Mapper
public interface CustomerMapper {
    /**
     * 顧客情報を全件取得します。
     * @return 顧客情報のリスト
     */
    @Select("""
            SELECT id, first_name, last_name, mail_address, birthday
            FROM customer ORDER BY id
            """)
    List<Customer> selectAll();

    /**
     * 顧客情報を登録します。
     * @param customer 顧客情報
     */
    @Insert("""
            INSERT INTO customer(first_name, last_name, mail_address, birthday)
            VALUES(#{firstName}, #{lastName}, #{mailAddress}, #{birthday})
            """)
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(Customer customer);
}
