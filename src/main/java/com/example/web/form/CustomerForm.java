package com.example.web.form;

import com.example.persistence.entity.Customer;
import com.example.web.constraint.Birthday;
import com.example.web.constraint.MailAddress;
import com.example.web.constraint.FirstName;
import com.example.web.constraint.LastName;

import java.time.LocalDate;

/**
 * 顧客情報のフォームクラスです。
 * @param firstName 名
 * @param lastName 姓
 * @param mailAddress メールアドレス
 * @param birthday 誕生日
 */
public record CustomerForm(@FirstName String firstName,
                           @LastName String lastName,
                           @MailAddress String mailAddress,
                           @Birthday String birthday) {

    /**
     * フィールドがすべて空（null）のCustomerFormインスタンスを生成します。
     */
    public static CustomerForm empty() {
        return new CustomerForm(null, null, null, null);
    }

    /**
     * エンティティのCustomerに変換します。
     */
    public Customer toEntity() {
        return new Customer(firstName, lastName, mailAddress, LocalDate.parse(birthday));
    }
}
