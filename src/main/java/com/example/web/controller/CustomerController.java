package com.example.web.controller;

import com.example.persistence.entity.Customer;
import com.example.service.CustomerService;
import com.example.web.form.CustomerForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 社員情報に関するコントローラークラスです。
 */
@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 社員一覧画面に遷移するコントローラーメソッドです。
     */
    @GetMapping("/")
    public String index(Model model) {
        // 全ての社員を取得
        List<Customer> customerList = customerService.findAll();
        // 一覧画面に社員リストを渡す
        model.addAttribute("customerList", customerList);
        // 一覧画面に遷移
        return "index";
    }

    /**
     * 社員追加画面に遷移するコントローラーメソッドです。
     */
    @GetMapping("/saveMain")
    public String saveMain(Model model) {
        // フィールドが全てnullのフォームインスタンスを追加する
        model.addAttribute(CustomerForm.empty());
        return "saveMain";
    }

    /**
     * 社員の追加を行うコントローラーメソッドです。
     */
    @PostMapping("/saveComplete")
    public String saveComplete(
            @Validated CustomerForm customerForm,
            BindingResult bindingResult) {
        // 入力エラーがある場合は追加画面に戻る
        if (bindingResult.hasErrors()) {
            return "saveMain";
        }
        // フォームをエンティティに変換
        Customer customer = customerForm.toEntity();
        // 追加を実行
        customerService.save(customer);
        // 一覧画面にリダイレクト
        return "redirect:/";
    }
}
