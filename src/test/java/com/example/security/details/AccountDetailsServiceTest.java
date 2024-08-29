package com.example.security.details;

import com.example.persistence.entity.Account;
import com.example.persistence.mapper.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountDetailsServiceTest {

    AccountDetailsService accountDetailsService;

    AccountMapper accountMapper;

    @BeforeEach
    void beforeEach() {
        accountMapper = mock(AccountMapper.class);
        accountDetailsService = new AccountDetailsService(accountMapper);
    }

    @Nested
    @DisplayName("loadUserByUsername()")
    class LoadUserByUsername {
        @Test
        @DisplayName("存在するメールアドレスを指定すると、AccountDetailsが返る")
        void success() {
            when(accountMapper.selectByMailAddress(any()))
                    .thenReturn(Optional.of(new Account(1, "user", "user@example.com", "user")));
            when(accountMapper.selectAuthoritiesByMailAddress("user@example.com"))
                    .thenReturn(List.of("ROLE_USER"));
            AccountDetails actual = (AccountDetails) accountDetailsService.loadUserByUsername("user@example.com");
            assertEquals(new AccountDetails(new Account(1, "user", "user@example.com", "user"),
                    AuthorityUtils.createAuthorityList(List.of("ROLE_USER"))), actual);
        }

        @Test
        @DisplayName("存在しないメールアドレスを指定すると、UsernameNotFoundExceptionが発生")
        void error() {
            when(accountMapper.selectByMailAddress(any()))
                    .thenReturn(Optional.empty());
            when(accountMapper.selectAuthoritiesByMailAddress(any()))
                    .thenReturn(List.of());
            assertThrows(UsernameNotFoundException.class, () -> {
                accountDetailsService.loadUserByUsername("hoge@example.com");
            });
        }
    }
}
