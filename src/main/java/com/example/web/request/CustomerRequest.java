package com.example.web.request;

import com.example.persistence.entity.Customer;
import com.example.web.constraint.Birthday;
import com.example.web.constraint.FirstName;
import com.example.web.constraint.LastName;

import com.example.web.constraint.MailAddress;
import java.time.LocalDate;

/**
 * 顧客情報のリクエストJSONから変換されるクラスです。
 * @param firstName 名
 * @param lastName 姓
 * @param mailAddress メールアドレス
 * @param mailAddress 誕生日
 */
public record CustomerRequest(
        @FirstName String firstName,
        @LastName String lastName,
        @MailAddress String mailAddress,
        @Birthday String birthday
) {
    /**
     * Customerエンティティに変換します。
     * @return Customerエンティティ
     */
    public Customer toEntity() {
        return new Customer(firstName, lastName, mailAddress, LocalDate.parse(birthday));
    }
}
