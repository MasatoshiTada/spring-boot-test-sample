package com.example.persistence.entity;

/**
 * システムのアカウントを表すエンティティです。
 * @param id アカウントID
 * @param name 名前
 * @param mailAddress メールアドレス
 * @param password パスワード
 */
public record Account(
        Integer id,
        String name,
        String mailAddress,
        String password
) {
}
