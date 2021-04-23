package com.example.web.security.details;

import com.example.web.persistence.entity.Account;
import com.example.web.persistence.mapper.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountDetailsServiceTest {

    AccountDetailsService accountDetailsService;
    AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = mock(AccountMapper.class);
        accountDetailsService = new AccountDetailsService(accountMapper);
    }

    @Test
    void 存在するユーザー名でAccountDetailsが返る() {
        String email = "user@example.com";
        Account account = new Account(1, "user", email, "user");
        List<String> roleList = List.of("ROLE_USER");
        when(accountMapper.findByEmail(email))
                .thenReturn(account);
        when(accountMapper.findAuthoritiesByEmail(email))
                .thenReturn(roleList);
        AccountDetails accountDetails = (AccountDetails) accountDetailsService.loadUserByUsername(email);
        assertAll(
                () -> assertNotNull(accountDetails.getAccount()),
                () -> assertEquals(account.getEmail(), accountDetails.getUsername()),
                () -> assertEquals(account.getPassword(), accountDetails.getPassword()),
                () -> assertEquals(1, accountDetails.getAuthorities().size()),
                () -> assertTrue(accountDetails.isAccountNonExpired()),
                () -> assertTrue(accountDetails.isAccountNonLocked()),
                () -> assertTrue(accountDetails.isCredentialsNonExpired()),
                () -> assertTrue(accountDetails.isEnabled())
                );
    }

    @Test
    void 存在しないユーザー名でUsernameNotFoundExceptionが発生() {
        String email = "user@example.com";
        when(accountMapper.findByEmail(email))
                .thenReturn(new Account(1, "user", email, "user"));
        when(accountMapper.findAuthoritiesByEmail(email))
                .thenReturn(List.of("ROLE_USER"));
        assertThrows(UsernameNotFoundException.class, () -> {
            accountDetailsService.loadUserByUsername("hoge@example.com");
        });
    }
}
