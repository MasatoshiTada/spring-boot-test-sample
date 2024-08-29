package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン画面に関するコントローラークラスです。
 */
@Controller
public class LoginController {

    /**
     * ログイン画面に遷移するコントローラーメソッドです。
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
