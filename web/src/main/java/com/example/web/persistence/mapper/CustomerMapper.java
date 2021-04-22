package com.example.web.persistence.mapper;

import com.example.web.persistence.entity.Customer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {
    @Select("SELECT id, first_name, last_name, email, birthday" +
            " FROM customer ORDER BY id")
    List<Customer> findAll();

    @Insert("INSERT INTO customer(first_name, last_name, email, birthday)" +
            " VALUES(#{firstName}, #{lastName}, #{email}, #{birthday})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void save(Customer customer);
}
