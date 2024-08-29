package com.example.security.details;

import com.example.persistence.entity.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Accountを保持するUserDetails実装クラスです。
 */
public record AccountDetails(
        Account account,
        Collection<? extends GrantedAuthority> authorityList
) implements UserDetails {

    @Override
    public String getUsername() {
        return account.mailAddress();
    }

    @Override
    public String getPassword() {
        return account.password();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
