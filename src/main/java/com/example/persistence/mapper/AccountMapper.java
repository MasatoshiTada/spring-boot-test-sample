package com.example.persistence.mapper;

import com.example.persistence.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * システムのアカウントを操作するマッパーインターフェースです。
 */
@Mapper
public interface AccountMapper {

    /**
     * メールアドレスでアカウントを検索します。
     * @param mailAddress
     * @return アカウント
     */
    @Select("""
            SELECT id, name, mail_address, password FROM account
            WHERE mail_address = #{mailAddress}
            """)
    Optional<Account> selectByMailAddress(String mailAddress);

    /**
     * メールアドレスでアカウントの権限を検索します。
     * @param mailAddress メールアドレス
     * @return 権限のリスト
     */
    @Select("""
            SELECT auth.authority_name FROM account acc
            JOIN account_authority auth ON acc.id = auth.account_id
            WHERE mail_address = #{mailAddress}
            """)
    List<String> selectAuthoritiesByMailAddress(String mailAddress);
}
