package com.example.security.details;

import com.example.persistence.entity.Account;
import com.example.persistence.mapper.AccountMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ユーザー情報を検索するUserDetailsService実装クラスです。
 */
@Service
public class AccountDetailsService implements UserDetailsService {

    private final AccountMapper accountMapper;

    public AccountDetailsService(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    /**
     * メールアドレスでユーザー情報を検索します。
     * @param mailAddress メールアドレス
     * @return ユーザー情報を保持するUserDetails実装
     * @throws UsernameNotFoundException メールアドレスに該当するユーザーが存在しない場合
     */
    @Override
    public UserDetails loadUserByUsername(String mailAddress) throws UsernameNotFoundException {
        // メールアドレスでユーザー情報を検索
        Optional<Account> accountOptional = accountMapper.selectByMailAddress(mailAddress);
        // ユーザーが存在しない場合は例外をスロー
        Account account = accountOptional.orElseThrow(() -> new UsernameNotFoundException("user not found"));
        // ユーザーの権限を取得
        List<String> authorityList = accountMapper.selectAuthoritiesByMailAddress(mailAddress);
        // UserDetails実装クラスに変換して返す
        AccountDetails accountDetails = new AccountDetails(account, AuthorityUtils.createAuthorityList(authorityList));
        return accountDetails;
    }
}
