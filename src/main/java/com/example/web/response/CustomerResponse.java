package com.example.web.response;

import com.example.persistence.entity.Customer;

import java.time.LocalDate;

/**
 * 顧客情報のレスポンスJSONを表すレコードクラスです。
 * @param id ID
 * @param firstName 名
 * @param lastName 姓
 * @param mailAddress メールアドレス
 * @param birthday 誕生日
 */
public record CustomerResponse(
        Integer id,
        String firstName,
        String lastName,
        String mailAddress,
        LocalDate birthday
) {

    public CustomerResponse(Customer customer) {
        this(customer.id(), customer.firstName(), customer.lastName(), customer.mailAddress(), customer.birthday());
    }
}
