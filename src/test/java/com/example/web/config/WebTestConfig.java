package com.example.web.config;

import com.example.service.CustomerService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("web-test")
public class WebTestConfig {

    /**
     * サービスをモック化＋Bean定義
     */
    @MockBean
    CustomerService customerService;
}
