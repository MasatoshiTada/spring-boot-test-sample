package com.example.web;

import com.gargoylesoftware.htmlunit.WebClient;
import org.junit.jupiter.api.Nested;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    WebClient webClient;

    @Nested
    class トップ画面へのアクセス {

    }

}
