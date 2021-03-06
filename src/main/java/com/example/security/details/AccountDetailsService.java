package com.example.security.details;

import com.example.persistence.entity.Account;
import com.example.persistence.mapper.AccountMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountMapper accountMapper;

    // AccountRepositoryをDIする（@Autowired不要）
    public AccountDetailsService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    // loadUserByUsername()をオーバーライド
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.findByEmail(username);
        List<String> authorities = accountMapper.findAuthoritiesByEmail(username);
        if (account == null) {
            throw new UsernameNotFoundException("user not found");
        }
        account.setAuthorities(authorities);
        AccountDetails accountDetails = new AccountDetails(account);
        return accountDetails;
    }
}
