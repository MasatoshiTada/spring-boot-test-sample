package com.example.web.web.rest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/csrf")
public class CsrfTokenRestController {

    @GetMapping
    public CsrfToken csrf(CsrfToken token) {
        return token;
    }
}
