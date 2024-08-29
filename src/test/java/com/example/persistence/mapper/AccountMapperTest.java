package com.example.persistence.mapper;

import com.example.annotation.MapperTest;
import com.example.persistence.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MapperTest
public class AccountMapperTest {

    @Autowired
    AccountMapper accountMapper;

    @Nested
    @DisplayName("selectByMailAddress()")
    @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
    class SelectByMailAddress {
        @Test
        @DisplayName("存在するメールアドレスを指定すると、該当するアカウントが取得できる")
        void success() {
            Optional<Account> actual = accountMapper.selectByMailAddress("user@example.com");
            assertEquals(Optional.of(new Account(1, "user", "user@example.com", "$2a$10$wdVyuUaOrZTawx4Z7LvqeOUlY2k4NzhPyjHmbMEEaIePCgyUnUaPG")), actual);
        }

        @Test
        @DisplayName("存在しないメールアドレスを指定すると、空のOptionalが返る")
        void empty() {
            Optional<Account> actual = accountMapper.selectByMailAddress("nono@example.com");
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    @DisplayName("selectAuthoritiesByMailAddress")
    @Sql({"classpath:schema.sql", "classpath:test-data.sql"})
    class SelectAuthoritiesByMailAddress {
        @Test
        @DisplayName("存在するメールアドレスを指定すると、該当する権限のリストが取得できる")
        void success() {
            List<String> actual = accountMapper.selectAuthoritiesByMailAddress("admin@example.com");
            assertEquals(List.of("ROLE_USER", "ROLE_ADMIN"), actual);
        }

        @Test
        @DisplayName("存在しないメールアドレスを指定すると、空のリストが取得できる")
        void empty() {
            List<String> actual = accountMapper.selectAuthoritiesByMailAddress("nono@example.com");
            assertTrue(actual.isEmpty());
        }
    }
}
