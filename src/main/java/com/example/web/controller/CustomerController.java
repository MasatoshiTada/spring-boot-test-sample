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

@Controller
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 社員一覧画面に遷移するコントローラーメソッド。
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "index";
    }

    /**
     * 社員追加画面に遷移するコントローラーメソッド。
     */
    @GetMapping("/insertMain")
    public String insertMain(Model model) {
        // フィールドが全てnullのフォームインスタンスを追加する
        model.addAttribute(CustomerForm.createEmptyForm());
        return "insertMain";
    }

    /**
     * 社員の追加を行うコントローラーメソッド。
     */
    @PostMapping("/insertComplete")
    public String insertComplete(
            @Validated CustomerForm customerForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "insertMain";
        }
        // フォームをエンティティに変換
        Customer customer = customerForm.convertToEntity();
        customerService.save(customer);
        return "redirect:/";
    }
}
