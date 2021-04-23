package com.example.persistence.mapper;

import com.example.persistence.entity.Account;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountMapperTest {

    @Autowired
    AccountMapper accountMapper;

    @Test
    void findByEmailメソッドで存在するメールアドレスを指定するとAccountを取得できる() {
        String email = "user@example.com";
        Account account = accountMapper.findByEmail(email);
        assertEquals("user", account.getName());
    }

    @Test
    void findByEmailメソッドで存在しないメールアドレスを指定するとnull() {
        String email = "nono@example.com";
        Account account = accountMapper.findByEmail(email);
        assertNull(account);
    }

}
