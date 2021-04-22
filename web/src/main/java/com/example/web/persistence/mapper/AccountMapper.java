package com.example.web.persistence.mapper;

import com.example.web.persistence.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Select("SELECT id, name, email, password FROM account" +
            " WHERE email = #{email}")
    Account findByEmail(String email);

    @Select("SELECT auth.authority_name FROM account acc" +
            " JOIN account_authority auth ON acc.id = auth.account_id" +
            " WHERE email = #{email}")
    List<String> findAuthoritiesByEmail(String email);
}
